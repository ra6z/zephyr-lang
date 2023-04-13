package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import io.ra6.zephyr.codeanalysis.syntax.expressions.ConditionalExpressionSyntax;
import lombok.Getter;

public class BoundConditionalExpression extends BoundExpression {
    @Getter
    private final BoundExpression condition;
    @Getter
    private final BoundExpression thenExpression;
    @Getter
    private final BoundExpression elseExpression;

    public BoundConditionalExpression(SyntaxNode syntax, BoundExpression condition, BoundExpression thenExpression, BoundExpression elseExpression) {
        super(syntax);
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;
    }

    @Override
    public TypeSymbol getType() {
        return thenExpression.getType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.CONDITIONAL_EXPRESSION;
    }
}
