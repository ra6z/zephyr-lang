package io.ra6.zephyr;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        mixinStandardHelpOptions = true,
        subcommands = {
                ZephyrRun.class
        }
)
public class Zephyr implements Callable<Void> {
    @Override
    public Void call() throws Exception {
        CommandLine.usage(this, System.out);
        return null;
    }
}
