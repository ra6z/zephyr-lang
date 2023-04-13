package io.ra6.zephyr;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        mixinStandardHelpOptions = true,
        subcommands = {
                ZephyrCompile.class,
                ZephyrRun.class
        }
)
public class Zephyr implements Callable<Void> {
    @Override
    public Void call() throws Exception {
        return null;
    }
}
