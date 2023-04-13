package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import io.ra6.zephyr.codeanalysis.syntax.expressions.AssignmentExpressionSyntax;
import lombok.Getter;

public class BoundAssignmentExpression extends BoundExpression {
    @Getter
    private final BoundExpression target;
    @Getter
    private final BoundExpression expression;

    public BoundAssignmentExpression(SyntaxNode syntax, BoundExpression target, BoundExpression expression) {
        super(syntax);
        this.target = target;
        this.expression = expression;
    }

    @Override
    public TypeSymbol getType() {
        return target.getType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.ASSIGNMENT_EXPRESSION;
    }
}
