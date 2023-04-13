package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

public class BoundBinaryExpression extends BoundExpression {
    @Getter
    private final BoundExpression left;
    @Getter
    private final String operator;
    @Getter
    private final BoundExpression right;
    @Getter
    private final TypeSymbol resultType;

    public BoundBinaryExpression(SyntaxNode syntax, BoundExpression left, String operator, BoundExpression right, TypeSymbol resultType) {
        super(syntax);
        this.left = left;
        this.operator = operator;
        this.right = right;
        this.resultType = resultType;
    }

    @Override
    public TypeSymbol getType() {
        return resultType;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.BINARY_EXPRESSION;
    }
}
