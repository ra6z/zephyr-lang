package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.List;

public class ArraySizeClauseSyntax extends SyntaxNode {

    @Getter
    private final SyntaxToken openBracket;
    @Getter
    private final ExpressionSyntax size;
    @Getter
    private final SyntaxToken closeBracket;

    public ArraySizeClauseSyntax(SyntaxTree syntaxTree, SyntaxToken openBracket, ExpressionSyntax size, SyntaxToken closeBracket) {
        super(syntaxTree);
        this.openBracket = openBracket;
        this.size = size;
        this.closeBracket = closeBracket;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.ARRAY_SIZE_CLAUSE;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(openBracket, size, closeBracket);
    }
}
