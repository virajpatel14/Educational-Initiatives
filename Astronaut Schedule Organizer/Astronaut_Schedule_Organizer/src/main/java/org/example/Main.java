package org.example;

import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ScheduleManager manager = ScheduleManager.getInstance();
        Scanner scanner = new Scanner(System.in);

        // Run the menu loop
        runMenuLoop(manager, scanner);
    }

    // Method to display the menu and handle user input
    private static void runMenuLoop(ScheduleManager manager, Scanner scanner) {
        boolean exit = false;

        while (!exit) {  // Controlled by user input
            // Display the menu
            displayMenu();

            System.out.print("Enter your choice: ");
            int choice = 0;

            // Handle input with exception handling
            try {
                choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Restart the loop
            }

            // Handle user's choice
            switch (choice) {
                case 1:
                    addTask(manager, scanner);
                    break;

                case 2:
                    removeTask(manager, scanner);
                    break;

                case 3:
                    editTask(manager, scanner);
                    break;

                case 4:
                    markTaskAsCompleted(manager, scanner);
                    break;

                case 5:
                    viewTasksByPriority(manager, scanner);
                    break;

                case 6:
                    manager.showAllTasks();
                    break;

                case 7:
                    System.out.println("Exiting...");
                    exit = true;  // Exit loop based on user choice
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    // Method to display the menu
    private static void displayMenu() {
        System.out.println("\n--- Astronaut Schedule Organizer ---");
        System.out.println("1. Add Task");
        System.out.println("2. Remove Task");
        System.out.println("3. Edit Task");
        System.out.println("4. Mark Task as Completed");
        System.out.println("5. View Tasks by Priority");
        System.out.println("6. View All Tasks");
        System.out.println("7. Exit");
    }

    // Method to handle adding a new task
    private static void addTask(ScheduleManager manager, Scanner scanner) {
        System.out.print("Enter Task Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Start Time (HH:MM): ");
        String startTime = scanner.nextLine();
        System.out.print("Enter End Time (HH:MM): ");
        String endTime = scanner.nextLine();
        System.out.print("Enter Priority (High, Medium, Low): ");
        String priority = scanner.nextLine();

        try {
            Task task = TaskFactory.createTask(description, startTime, endTime, priority);
            manager.addTask(task);
        } catch (Exception e) {
            Logger.logError(e.getMessage());
        }
    }

    // Method to handle removing a task
    private static void removeTask(ScheduleManager manager, Scanner scanner) {
        System.out.print("Enter Task Description to Remove: ");
        String taskToRemove = scanner.nextLine();
        manager.removeTask(taskToRemove);
    }

    // Method to handle editing an existing task
    private static void editTask(ScheduleManager manager, Scanner scanner) {
        System.out.print("Enter Task Description to Edit: ");
        String taskToEdit = scanner.nextLine();
        System.out.print("Enter New Task Description: ");
        String newDescription = scanner.nextLine();
        System.out.print("Enter New Start Time (HH:MM): ");
        String newStartTime = scanner.nextLine();
        System.out.print("Enter New End Time (HH:MM): ");
        String newEndTime = scanner.nextLine();
        System.out.print("Enter New Priority (High, Medium, Low): ");
        String newPriority = scanner.nextLine();

        try {
            LocalTime newStart = LocalTime.parse(newStartTime);
            LocalTime newEnd = LocalTime.parse(newEndTime);
            manager.editTask(taskToEdit, newDescription, newStart, newEnd, newPriority);
        } catch (Exception e) {
            Logger.logError("Error: " + e.getMessage());
        }
    }

    // Method to mark a task as completed
    private static void markTaskAsCompleted(ScheduleManager manager, Scanner scanner) {
        System.out.print("Enter Task Description to Mark as Completed: ");
        String taskToMark = scanner.nextLine();
        manager.markTaskAsCompleted(taskToMark);
    }

    // Method to view tasks by priority
    private static void viewTasksByPriority(ScheduleManager manager, Scanner scanner) {
        System.out.print("Enter Priority (High, Medium, Low): ");
        String taskPriority = scanner.nextLine();
        manager.viewTasksByPriority(taskPriority);
    }
}
