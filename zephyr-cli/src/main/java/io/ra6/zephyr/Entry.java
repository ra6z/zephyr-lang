package io.ra6.zephyr;

import picocli.CommandLine;

public final class Entry {
    public static void main(String[] args) {
//        boolean verbose = false;

//        args = "run D:\\Projects\\zephyr\\examples\\dictionary.zph -t -std D:\\Projects\\zephyr\\stdlib".concat(verbose ? "-v" : "").split(" ");

        int exitCode = new CommandLine(new Zephyr()).execute(args);
        System.exit(exitCode);
    }
}
