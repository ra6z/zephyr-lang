package io.ra6.zephyr.compiling;

import io.ra6.zephyr.library.ZephyrLibrary;
import io.ra6.zephyr.library.ZephyrLibraryMetadata;
import io.ra6.zephyr.writer.DiagnosticWriter;
import lombok.experimental.ExtensionMethod;

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
