package io.ra6.zephyr;

import io.ra6.zephyr.compiling.Compiler;
import io.ra6.zephyr.compiling.CompilerFlags;
import io.ra6.zephyr.evaluating.EvaluationResult;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "run", description = "Runs a given zephyr source file")
public class ZephyrRun implements Callable<Void> {
    @CommandLine.Parameters(description = "Zephyr source files to compile")
    private String sourceFile;

    @CommandLine.Option(names = {"-t", "--tree"}, description = "Prints the syntax tree")
    private boolean printTree;

    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Prints verbose output")
    private boolean verbose;

    @CommandLine.Option(names = {"-std", "--standard-library"}, description = "Path to standard library")
    private String standardLibraryPath = System.getenv("ZEPHYR_STANDARD_LIBRARY");

    @CommandLine.Parameters(description = "Additional arguments to pass to the program")
    private String[] additionalArgs = new String[0];

    @Override
    public Void call() throws Exception {
        if (standardLibraryPath == null) {
            System.out.print(ConsoleColors.ANSI_RED);
            System.out.println("Standard library path not specified");
            System.out.print(ConsoleColors.ANSI_RESET);
            return null;
        }

        System.out.printf("Running program %s%n", sourceFile);

        int flags = 0;
        if (verbose) flags |= CompilerFlags.VERBOSE;
        if (printTree) flags |= CompilerFlags.PRINT_TREE;

        Compiler compiler = new Compiler(standardLibraryPath);
        EvaluationResult result = compiler.run(this.sourceFile, flags, additionalArgs);

        System.out.printf("Program finished with exit code %s%n", result.getExitCode());

        return null;
    }
}
