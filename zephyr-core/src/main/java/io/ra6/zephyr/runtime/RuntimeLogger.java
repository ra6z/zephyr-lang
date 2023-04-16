package io.ra6.zephyr.runtime;

import io.ra6.zephyr.ConsoleColors;

import java.io.PrintStream;

public class RuntimeLogger {
    public static final int NONE = 0;
    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;

    public static int LOG_LEVEL = INFO;

    public static PrintStream OUT = System.out;

    private static final String DEBUG_COLOR = ConsoleColors.ANSI_BRIGHT_YELLOW;
    private static final String ERROR_COLOR = ConsoleColors.ANSI_BRIGHT_RED;
    private static final String INFO_COLOR = ConsoleColors.ANSI_BRIGHT_GREEN;
    private static final String TRACE_COLOR = ConsoleColors.ANSI_BRIGHT_BLUE;


    public static void debugf(String format, Object... args) {
        if (LOG_LEVEL < DEBUG) return;

        String prefix = String.format("%s[DEBUG]%s >> ", DEBUG_COLOR, ConsoleColors.ANSI_RESET);
        OUT.printf(prefix + format + "%n", args);
    }

    public static void errorf(String format, Object... args) {
        String prefix = String.format("%s[ERROR]%s >> ", ERROR_COLOR, ConsoleColors.ANSI_RESET);
        OUT.printf(prefix + format + "%n", args);
    }

    public static void infof(String format, Object... args) {
        if (LOG_LEVEL < INFO) return;

        String prefix = String.format("%s[INFO]%s >> ", INFO_COLOR, ConsoleColors.ANSI_RESET);
        OUT.printf(prefix + format + "%n", args);
    }

    public static void tracef(String format, Object... args) {
        if (LOG_LEVEL < TRACE) return;

        String prefix = String.format("%s[TRACE]%s >> ", TRACE_COLOR, ConsoleColors.ANSI_RESET);
        OUT.printf(prefix + format + "%n", args);
    }

}
