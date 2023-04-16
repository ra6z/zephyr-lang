package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

public class BoundArrayCreationExpression extends BoundExpression {
    private final TypeSymbol type;
    @Getter
    private final HashMap<BoundExpression, BoundExpression> dimensions;

    public BoundArrayCreationExpression(SyntaxNode syntax, TypeSymbol type, HashMap<BoundExpression, BoundExpression> dimensions) {
        super(syntax);
        this.type = type;
        this.dimensions = dimensions;
    }

    @Override
    public TypeSymbol getType() {
        return type;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.ARRAY_CREATION_EXPRESSION;
    }
}
