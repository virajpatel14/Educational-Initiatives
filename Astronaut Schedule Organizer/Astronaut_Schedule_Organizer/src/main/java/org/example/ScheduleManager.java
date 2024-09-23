package org.example;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Optional;

public class ScheduleManager {
    private static ScheduleManager instance;
    private List<Task> tasks;
    private ConflictObserver conflictObserver;

    private ScheduleManager() {
        tasks = new ArrayList<>();
        conflictObserver = new ConflictObserver();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    // Method to add a task with unique name enforcement
    public void addTask(Task newTask) {
        Optional<Task> existingTask = findTaskByName(newTask.getDescription());

        // Ensure task names are unique
        if (existingTask.isPresent()) {
            throw new IllegalArgumentException("Task with the name '" + newTask.getDescription() + "' already exists.");
        }

        // Handle conflicts with other tasks
        List<Task> conflictingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.conflictsWith(newTask)) {
                conflictingTasks.add(task);
            }
        }

        // If conflicts, resolve by priority
        if (!conflictingTasks.isEmpty()) {
            for (Task conflict : conflictingTasks) {
                int comparison = comparePriority(newTask.getPriority(), conflict.getPriority());

                if (comparison > 0) {
                    tasks.remove(conflict);
                    System.out.println("Conflict detected. Task '" + conflict.getDescription() + "' removed due to lower priority.");
                } else {
                    conflictObserver.notifyConflict(conflict.getDescription());
                    throw new IllegalArgumentException("Task conflicts with existing task: " + conflict.getDescription() +
                            ". Priority too low to replace.");
                }
            }
        }

        tasks.add(newTask);
        tasks.sort(Comparator.comparing(Task::getStartTime));
        System.out.println("Task added successfully.");
    }

    public void removeTask(String description) {
        Optional<Task> taskToRemove = findTaskByName(description);

        if (taskToRemove.isPresent()) {
            tasks.remove(taskToRemove.get());
            System.out.println("Task '" + description + "' removed successfully.");
        } else {
            System.out.println("Task with the name '" + description + "' not found.");
        }
    }


    // Method to edit an existing task by name
    public void editTask(String name, String newDescription, LocalTime newStartTime, LocalTime newEndTime, String newPriority) {
        Optional<Task> taskToEdit = findTaskByName(name);

        if (taskToEdit.isPresent()) {
            Task task = taskToEdit.get();
            task.setDescription(newDescription);
            task.setStartTime(newStartTime);
            task.setEndTime(newEndTime);
            task.setPriority(newPriority);
            System.out.println("Task '" + name + "' edited successfully.");
        } else {
            System.out.println("Task with the name '" + name + "' not found.");
        }
    }

    // Method to mark a task as completed
    public void markTaskAsCompleted(String name) {
        Optional<Task> task = findTaskByName(name);

        if (task.isPresent()) {
            task.get().markAsCompleted();
            System.out.println("Task '" + name + "' marked as completed.");
        } else {
            System.out.println("Task with the name '" + name + "' not found.");
        }
    }

    // Method to view tasks by priority
    public void viewTasksByPriority(String priority) {
        System.out.println("Tasks with priority " + priority + ":");
        tasks.stream()
                .filter(task -> task.getPriority().equalsIgnoreCase(priority))
                .forEach(System.out::println);
    }

    // Find a task by its name (optional return)
    private Optional<Task> findTaskByName(String name) {
        return tasks.stream()
                .filter(task -> task.getDescription().equalsIgnoreCase(name))
                .findFirst();
    }

    public void showAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
        } else {
            tasks.forEach(System.out::println);
        }
    }

    // Helper method to compare priority levels
    private int comparePriority(String priority1, String priority2) {
        int rank1 = getPriorityRank(priority1);
        int rank2 = getPriorityRank(priority2);
        return Integer.compare(rank1, rank2);
    }

    // Helper method to get numeric rank of priorities
    private int getPriorityRank(String priority) {
        switch (priority.toLowerCase()) {
            case "high":
                return 3;
            case "medium":
                return 2;
            case "low":
                return 1;
            default:
                throw new IllegalArgumentException("Invalid priority level: " + priority);
        }
    }
}