package io.ra6.zephyr.library;

import io.ra6.zephyr.codeanalysis.binding.Binder;
import io.ra6.zephyr.codeanalysis.binding.BoundProgram;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxTree;
import io.ra6.zephyr.sourcefile.SourceText;

import java.io.File;
import java.util.HashMap;

public class ZephyrLibrary {
    private final HashMap<String, String> libraryMapping = new HashMap<>();
    private final ZephyrLibraryMetadata metadata;

    private final HashMap<String, BoundProgram> boundPrograms = new HashMap<>();

    public ZephyrLibrary(ZephyrLibraryMetadata metadata) {
        this.metadata = metadata;

        loadLibrary();
    }

    private void loadLibrary() {
        File libraryDirectory = new File(metadata.path());
        if (!libraryDirectory.isDirectory()) {
            throw new RuntimeException("Library path is not a directory");
        }

        File[] libraryFiles = libraryDirectory.listFiles();
        if (libraryFiles == null) {
            throw new RuntimeException("Library directory is empty");
        }

        for (File file : libraryFiles) {
            if (file.isDirectory()) {
                loadLibraryDirectory(file);
            } else {
                loadLibraryFile(file);
            }
        }
    }

    private void loadLibraryDirectory(File file) {
        if (!file.isDirectory()) {
            throw new RuntimeException("Library directory is not a directory");
        }

        File[] libraryFiles = file.listFiles();
        if (libraryFiles == null) {
            throw new RuntimeException("Library directory is empty");
        }

        for (File libraryFile : libraryFiles) {
            if (libraryFile.isDirectory()) {
                loadLibraryDirectory(libraryFile);
            } else {
                loadLibraryFile(libraryFile);
            }
        }
    }

    private void loadLibraryFile(File file) {
        if (!file.isFile()) {
            throw new RuntimeException("Library file is not a file");
        }

        if (!file.getName().endsWith(".zph")) {
            return;
        }

        String path = file.getPath().substring(metadata.path().length() + 1);
        String name = "%s:%s".formatted(metadata.prefix(), path.substring(0, path.length() - 4).replace("\\", "/"));
        libraryMapping.put(name, file.getPath());
    }

    public String getLibraryPath(String name) {
        return libraryMapping.get(name);
    }
}
