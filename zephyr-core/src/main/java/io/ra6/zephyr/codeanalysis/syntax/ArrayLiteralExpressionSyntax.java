package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ArrayLiteralExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final SyntaxToken leftBracket;
    @Getter
    private final SeparatedSyntaxList<ExpressionSyntax> elements;
    @Getter
    private final SyntaxToken rightBracket;

    public ArrayLiteralExpressionSyntax(SyntaxTree syntaxTree, SyntaxToken leftBracket, SeparatedSyntaxList<ExpressionSyntax> elements, SyntaxToken rightBracket) {
        super(syntaxTree);
        this.leftBracket = leftBracket;
        this.elements = elements;
        this.rightBracket = rightBracket;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.ARRAY_LITERAL_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> children = new ArrayList<>();
        children.add(leftBracket);
        children.addAll(elements.getNodesAndSeparators());
        children.add(rightBracket);
        return children;
    }
}
