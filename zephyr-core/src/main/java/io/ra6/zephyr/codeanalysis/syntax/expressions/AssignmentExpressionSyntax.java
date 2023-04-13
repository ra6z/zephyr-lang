package io.ra6.zephyr.codeanalysis.syntax.expressions;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class AssignmentExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final ExpressionSyntax left;
    @Getter
    private final SyntaxToken operatorToken;
    @Getter
    private final ExpressionSyntax right;

    public AssignmentExpressionSyntax(SyntaxTree syntaxTree, ExpressionSyntax left, SyntaxToken operatorToken, ExpressionSyntax right) {
        super(syntaxTree);
        this.left = left;
        this.operatorToken = operatorToken;
        this.right = right;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.ASSIGNMENT_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(left, operatorToken, right);
    }
}
