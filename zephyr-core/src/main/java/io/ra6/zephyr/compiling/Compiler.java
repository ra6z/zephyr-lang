package io.ra6.zephyr.compiling;

import io.ra6.zephyr.codeanalysis.binding.Binder;
import io.ra6.zephyr.codeanalysis.binding.BoundProgram;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxTree;
import io.ra6.zephyr.library.ZephyrLibrary;
import io.ra6.zephyr.library.ZephyrLibraryMetadata;
import io.ra6.zephyr.runtime.EvaluationResult;
import io.ra6.zephyr.runtime.Evaluator;
import io.ra6.zephyr.runtime.RuntimeLogger;
import io.ra6.zephyr.sourcefile.SourceText;
import io.ra6.zephyr.writer.DiagnosticWriter;
import io.ra6.zephyr.writer.SyntaxWriter;
import lombok.experimental.ExtensionMethod;

import java.io.IOException;

@ExtensionMethod({DiagnosticWriter.class})
public class Compiler {
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final ZephyrLibrary standardLibrary;

    public Compiler(String standardLibraryPath) {
        this.standardLibrary = new ZephyrLibrary(new ZephyrLibraryMetadata("Standard Library", "std", standardLibraryPath, "0.0.1", "rasix", ""));
    }

    public void compile(String inputPath, String outputPath, int flags) {
        throw new RuntimeException("Not implemented yet");
    }


}
