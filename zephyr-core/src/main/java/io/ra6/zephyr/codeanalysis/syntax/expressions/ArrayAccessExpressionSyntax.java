package io.ra6.zephyr.codeanalysis.syntax.expressions;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class ArrayAccessExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final ExpressionSyntax target;
    @Getter
    private final SyntaxToken leftBracketToken;
    @Getter
    private final ExpressionSyntax index;
    @Getter
    private final SyntaxToken rightBracketToken;

    public ArrayAccessExpressionSyntax(SyntaxTree syntaxTree, ExpressionSyntax target, SyntaxToken leftBracketToken, ExpressionSyntax index, SyntaxToken rightBracketToken) {
        super(syntaxTree);
        this.target = target;
        this.leftBracketToken = leftBracketToken;
        this.index = index;
        this.rightBracketToken = rightBracketToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.ARRAY_ACCESS_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(target, leftBracketToken, index, rightBracketToken);
    }
}
