package io.ra6.zephyr;

import io.ra6.zephyr.codeanalysis.binding.Binder;
import io.ra6.zephyr.codeanalysis.binding.BoundProgram;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxTree;
import io.ra6.zephyr.diagnostic.Diagnostic;
import io.ra6.zephyr.library.ZephyrLibrary;
import io.ra6.zephyr.library.ZephyrLibraryMetadata;
import io.ra6.zephyr.runtime.Interpreter;
import io.ra6.zephyr.runtime.Runtime;
import io.ra6.zephyr.runtime.RuntimeLogger;
import io.ra6.zephyr.sourcefile.SourceText;
import io.ra6.zephyr.writer.DiagnosticWriter;
import io.ra6.zephyr.writer.SyntaxWriter;
import lombok.experimental.ExtensionMethod;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.concurrent.Callable;

@ExtensionMethod({DiagnosticWriter.class, SyntaxWriter.class})
@CommandLine.Command(name = "run", description = "Runs a given zephyr source file")
public class ZephyrRun implements Callable<Void> {
    @CommandLine.Parameters(description = "Zephyr source files to compile")
    private String sourceFile;

    @CommandLine.Option(names = {"-t", "--tree"}, description = "Prints the syntax tree")
    private boolean printTree;

    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Sets the verbose level (none, info, debug)")
    private String verboseLevel = "none";

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

        // print program name with arguments
        System.out.print(ConsoleColors.ANSI_GREEN);
        System.out.print("zephyr run ");
        System.out.print(ConsoleColors.ANSI_RESET);
        System.out.print(sourceFile);
        for (String arg : Arrays.stream(additionalArgs).skip(1).toArray(String[]::new)) {
            System.out.print(" ");
            System.out.print(arg);
        }
        System.out.println();

        RuntimeLogger.LOG_LEVEL = RuntimeLogger.NONE;
        if (this.verboseLevel.equalsIgnoreCase("info")) {
            RuntimeLogger.LOG_LEVEL = RuntimeLogger.INFO;
            RuntimeLogger.infof("Setting verbose level to info");
        } else if (this.verboseLevel.equalsIgnoreCase("debug")) {
            RuntimeLogger.LOG_LEVEL = RuntimeLogger.DEBUG;
            RuntimeLogger.infof("Setting verbose level to debug");
        } else {
            RuntimeLogger.infof("Setting verbose level to none");
        }

        RuntimeLogger.infof("Preparing to run program...");
        ZephyrLibraryMetadata standardLibraryMeta = new ZephyrLibraryMetadata("Standard Library", "std", standardLibraryPath, "0.0.1", "rasix", "");
        ZephyrLibrary standardLibrary = new ZephyrLibrary(standardLibraryMeta);

        RuntimeLogger.infof("\tLoading source file...");
        SourceText mainSourceText = SourceText.fromFile(this.sourceFile);

        RuntimeLogger.infof("\tParsing program...");
        SyntaxTree mainTree = SyntaxTree.parse(mainSourceText);

        if (this.printTree) System.out.printTree(mainTree);

        RuntimeLogger.infof("\tBinding program...");
        Binder binder = new Binder(mainTree, standardLibrary);
        BoundProgram boundProgram = binder.bindProgram();

        RuntimeLogger.infof("\tPreparing runtime...");
        Runtime runtime = new Runtime();
        runtime.registerProgram(boundProgram.getProgramScope());
        runtime.setMainProgram(runtime.getProgram(boundProgram.getProgramScope().getName()));

        RuntimeLogger.infof("Finished preparing runtime. Running program...");

        if (binder.getDiagnostics().hasErrors()) {
            System.out.printDiagnostics(binder.getDiagnostics());
            return null;
        }

        if (binder.getDiagnostics().hasWarnings()) {
            // Skip warnings if the log level is less than info
            if (RuntimeLogger.LOG_LEVEL >= RuntimeLogger.INFO)
                System.out.printDiagnostics(binder.getDiagnostics().asList().stream().filter(Diagnostic::isWarning).toList());
        }

        Interpreter interpreter = new Interpreter(runtime, additionalArgs);
        interpreter.run();
        System.out.printf("Program finished with exit code %s%n", interpreter.getExitCode());
        return null;
    }
}
