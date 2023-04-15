package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.ArrayTypeSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

public class BoundArrayAccessExpression extends BoundExpression {
    @Getter
    private final BoundExpression target;
    @Getter
    private final BoundExpression index;

    public BoundArrayAccessExpression(SyntaxNode syntax, BoundExpression target, BoundExpression index) {
        super(syntax);
        this.target = target;
        this.index = index;
    }

    @Override
    public TypeSymbol getType() {
        ArrayTypeSymbol arrayType = (ArrayTypeSymbol) target.getType();
        return arrayType.getElementType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.ARRAY_ACCESS_EXPRESSION;
    }
}
