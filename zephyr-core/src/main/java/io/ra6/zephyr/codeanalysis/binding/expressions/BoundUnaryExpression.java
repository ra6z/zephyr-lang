package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import io.ra6.zephyr.codeanalysis.syntax.expressions.UnaryExpressionSyntax;
import lombok.Getter;

public class BoundUnaryExpression extends BoundExpression {
    @Getter
    private final String operator;
    @Getter
    private final BoundExpression operand;
    private final TypeSymbol resultType;

    public BoundUnaryExpression(SyntaxNode syntax, String operator, BoundExpression operand, TypeSymbol resultType) {
        super(syntax);
        this.operator = operator;
        this.operand = operand;
        this.resultType = resultType;
    }

    @Override
    public TypeSymbol getType() {
        return resultType;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.UNARY_EXPRESSION;
    }
}
