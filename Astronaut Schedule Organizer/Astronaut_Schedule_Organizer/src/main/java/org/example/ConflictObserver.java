package org.example;

public class ConflictObserver {
    public void notifyConflict(String taskDescription) {
        System.out.println("Conflict detected with task: " + taskDescription);
    }
}