package io.ra6.zephyr.compiling;

public class CompilerFlags {
    public static int VERBOSE = 1 << 0;
    public static int PRINT_TREE = 1 << 1;

    public static boolean isFlagSet(int flags, int flag) {
        return (flags & flag) == flag;
    }
}
