package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ArraySizeClauseSyntax extends SyntaxNode {

    @Getter
    private final SyntaxToken openBracket;
    @Getter
    private final ExpressionSyntax size;
    @Getter
    private final InitialArrayValueClauseSyntax initialArrayValueClause;
    @Getter
    private final SyntaxToken closeBracket;

    public ArraySizeClauseSyntax(SyntaxTree syntaxTree, SyntaxToken openBracket, ExpressionSyntax size, InitialArrayValueClauseSyntax initialArrayValueClause, SyntaxToken closeBracket) {
        super(syntaxTree);
        this.openBracket = openBracket;
        this.size = size;
        this.initialArrayValueClause = initialArrayValueClause;
        this.closeBracket = closeBracket;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.ARRAY_SIZE_CLAUSE;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>();
        result.add(openBracket);
        result.add(size);
        if (initialArrayValueClause != null) result.add(initialArrayValueClause);
        result.add(closeBracket);
        return result;
    }
}
