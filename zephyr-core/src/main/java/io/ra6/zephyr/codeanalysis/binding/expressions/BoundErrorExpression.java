package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;

public class BoundErrorExpression extends BoundExpression {
    public BoundErrorExpression(SyntaxNode syntax) {
        super(syntax);
    }

    @Override
    public TypeSymbol getType() {
        return Types.ERROR;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.ERROR_EXPRESSION;
    }
}
