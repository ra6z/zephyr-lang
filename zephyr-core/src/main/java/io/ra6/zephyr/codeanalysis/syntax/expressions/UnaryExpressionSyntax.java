package io.ra6.zephyr.codeanalysis.syntax.expressions;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class UnaryExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final SyntaxToken operatorToken;
    @Getter
    private final ExpressionSyntax operand;

    public UnaryExpressionSyntax(SyntaxTree syntaxTree, SyntaxToken operatorToken, ExpressionSyntax operand) {
        super(syntaxTree);
        this.operatorToken = operatorToken;
        this.operand = operand;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.UNARY_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(operatorToken, operand);
    }
}
