package io.ra6.zephyr;

import picocli.CommandLine;

public final class Entry {

    public static void main(String[] args) {
        args = "run D:\\Projects\\zephyr\\examples\\test.zph -v -t -std D:\\Projects\\zephyr\\std -- hello world".split(" ");

        int exitCode = new CommandLine(new Zephyr()).execute(args);
        System.exit(exitCode);
    }
}
