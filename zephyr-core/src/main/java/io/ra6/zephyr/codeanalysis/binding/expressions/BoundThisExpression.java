package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;

public class BoundThisExpression extends BoundExpression {
    private final TypeSymbol type;

    public BoundThisExpression(SyntaxNode syntax, TypeSymbol type) {
        super(syntax);
        this.type = type;
    }

    @Override
    public TypeSymbol getType() {
        return type;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.THIS_EXPRESSION;
    }
}
