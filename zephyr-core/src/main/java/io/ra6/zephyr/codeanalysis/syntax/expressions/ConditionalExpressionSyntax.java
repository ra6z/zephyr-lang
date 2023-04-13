package io.ra6.zephyr.codeanalysis.syntax.expressions;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class ConditionalExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final ExpressionSyntax condition;
    @Getter
    private final SyntaxToken questionToken;
    @Getter
    private final ExpressionSyntax thenExpression;
    @Getter
    private final SyntaxToken colonToken;
    @Getter
    private final ExpressionSyntax elseExpression;

    public ConditionalExpressionSyntax(SyntaxTree syntaxTree, ExpressionSyntax condition, SyntaxToken questionToken, ExpressionSyntax thenExpression, SyntaxToken colonToken, ExpressionSyntax elseExpression) {
        super(syntaxTree);
        this.condition = condition;
        this.questionToken = questionToken;
        this.thenExpression = thenExpression;
        this.colonToken = colonToken;
        this.elseExpression = elseExpression;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.CONDITIONAL_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(condition, questionToken, thenExpression, colonToken, elseExpression);
    }
}
