package io.ra6.zephyr.codeanalysis.binding;

import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

public abstract class BoundExpression extends BoundNode {
    protected BoundExpression(SyntaxNode syntax) {
        super(syntax);
    }

    public abstract TypeSymbol getType();
}
