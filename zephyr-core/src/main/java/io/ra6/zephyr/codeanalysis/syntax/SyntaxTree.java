package io.ra6.zephyr.codeanalysis.syntax;

import io.ra6.zephyr.sourcefile.SourceText;
import io.ra6.zephyr.diagnostic.Diagnostic;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

public class SyntaxTree {
    @Getter
    private final SourceText sourceText;
    @Getter
    private final List<Diagnostic> diagnostics;
    @Getter
    private final CompilationUnitSyntax root;

    private SyntaxTree(SourceText sourceText) {
        this.sourceText = sourceText;

        Parser parser = new Parser(this);
        root = parser.parseCompilationUnit();
        diagnostics = parser.getDiagnostics().asList();
    }

    /**
     * Loads a syntax tree from a given file path
     * @param filePath
     * @return
     * @throws IOException
     */
    public static SyntaxTree load(String filePath) throws IOException {
        SourceText file = SourceText.fromFile(filePath);
        return parse(file);
    }

    public static SyntaxTree parse(SourceText file) {
        return new SyntaxTree(file);
    }
}
