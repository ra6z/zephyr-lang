package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.List;

public class InitialArrayValueClauseSyntax extends SyntaxNode {
    @Getter
    private final SyntaxToken commaToken;
    @Getter
    private final ExpressionSyntax initializer;

    public InitialArrayValueClauseSyntax(SyntaxTree tree, SyntaxToken commaToken, ExpressionSyntax initializer) {
        super(tree);
        this.commaToken = commaToken;
        this.initializer = initializer;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.INITIAL_ARRAY_VALUE_CLAUSE;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(commaToken, initializer);
    }
}
