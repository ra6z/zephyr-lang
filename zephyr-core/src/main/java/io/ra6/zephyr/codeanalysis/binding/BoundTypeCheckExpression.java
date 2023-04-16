package io.ra6.zephyr.codeanalysis.binding;

import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

public class BoundTypeCheckExpression extends BoundExpression {
    @Getter
    private final BoundExpression leftExpression;
    @Getter
    private final TypeSymbol rightType;

    public BoundTypeCheckExpression(SyntaxNode syntax, BoundExpression leftExpression, TypeSymbol rightType) {
        super(syntax);
        this.leftExpression = leftExpression;
        this.rightType = rightType;
    }

    @Override
    public TypeSymbol getType() {
        return Types.BOOL;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.TYPE_CHECK_EXPRESSION;
    }
}
