package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.List;

public class NameExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final SyntaxToken identifier;

    public NameExpressionSyntax(SyntaxTree syntaxTree, SyntaxToken identifier) {
        super(syntaxTree);
        this.identifier = identifier;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.NAME_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(identifier);
    }
}
