package io.ra6.zephyr.runtime;

import java.io.PrintStream;

public class RuntimeLogger {
    public static boolean DEBUG = false;
    public static PrintStream OUT = System.out;

    public static void log(String message) {
        if (!DEBUG) return;

        OUT.println("DEBUG: " + message);
    }

    public static void printf(String format, Object... args) {
        if (!DEBUG) return;

        OUT.printf("DEBUG: " + format, args);
    }
}
