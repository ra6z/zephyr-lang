package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CompilationUnitSyntax extends SyntaxNode {
    @Getter
    private final List<StatementSyntax> statements;
    @Getter
    private final SyntaxToken endOfFileToken;

    public CompilationUnitSyntax(SyntaxTree tree, List<StatementSyntax> statements, SyntaxToken endOfFileToken) {
        super(tree);
        this.statements = statements;
        this.endOfFileToken = endOfFileToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.COMPILATION_UNIT;
    }
    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>(statements);
        result.add(endOfFileToken);
        return result;
    }
}
