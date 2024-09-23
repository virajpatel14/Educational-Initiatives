package org.example;

public class Logger {
    public static void log(String message) {
        System.out.println("[LOG] " + message);
    }

    public static void logError(String message) {
        System.err.println("[ERROR] " + message);
    }
}