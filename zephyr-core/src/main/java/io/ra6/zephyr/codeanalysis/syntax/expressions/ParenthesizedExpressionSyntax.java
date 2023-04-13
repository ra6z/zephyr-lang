package io.ra6.zephyr.codeanalysis.syntax.expressions;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class ParenthesizedExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final SyntaxToken leftParenthesisToken;
    @Getter
    private final ExpressionSyntax expression;
    @Getter
    private final SyntaxToken rightParenthesisToken;

    public ParenthesizedExpressionSyntax(SyntaxTree syntaxTree, SyntaxToken leftParenthesisToken, ExpressionSyntax expression, SyntaxToken rightParenthesisToken) {
        super(syntaxTree);
        this.leftParenthesisToken = leftParenthesisToken;
        this.expression = expression;
        this.rightParenthesisToken = rightParenthesisToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.PARENTHESIZED_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(leftParenthesisToken, expression, rightParenthesisToken);
    }
}
