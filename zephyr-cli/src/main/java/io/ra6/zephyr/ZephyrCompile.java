package io.ra6.zephyr;

import io.ra6.zephyr.compiling.Compiler;
import io.ra6.zephyr.compiling.CompilerFlags;
import io.ra6.zephyr.writer.DiagnosticWriter;
import io.ra6.zephyr.writer.SyntaxWriter;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "compile", description = "Compiles a given zephyr source file")
public class ZephyrCompile implements Callable<Void> {
    @CommandLine.Parameters(description = "Zephyr source files to compile")
    private String sourceFile;

    @CommandLine.Option(names = {"-o", "--output"}, description = "Output file name")
    private String output;

    @CommandLine.Option(names = {"-t", "--tree"}, description = "Prints the syntax tree")
    private boolean printTree;

    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Prints verbose output")
    private boolean verbose;

    @CommandLine.Option(names = {"-std", "--standard-library"}, description = "Path to standard library")
    private String standardLibraryPath = System.getenv("ZEPHYR_STANDARD_LIBRARY");

    @Override
    public Void call() throws Exception {
        if (standardLibraryPath == null) {
            // Get environment variable
            System.out.print(ConsoleColors.ANSI_RED);
            System.out.println("Standard library path not specified");
            System.out.print(ConsoleColors.ANSI_RESET);
            return null;
        }

        System.out.printf("Compiling %s -> %s%n", sourceFile, output);

        int flags = 0;
        if (verbose) flags |= CompilerFlags.VERBOSE;
        if (printTree) flags |= CompilerFlags.PRINT_TREE;

        Compiler compiler = new Compiler(standardLibraryPath);
        compiler.compile(this.sourceFile, this.output, flags);

        return null;
    }
}
