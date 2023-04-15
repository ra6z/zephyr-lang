package io.ra6.zephyr;

import picocli.CommandLine;

public final class Entry {
    public static void main(String[] args) {
//        boolean verbose = false;

//        args = "run -std D:/Projects/zephyr/stdlib D:/Projects/zephyr/examples/list.zph -t".split(" ");

        int exitCode = new CommandLine(new Zephyr()).execute(args);
        System.exit(exitCode);
    }
}
