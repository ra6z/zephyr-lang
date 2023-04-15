package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.ArrayTypeSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

import java.util.List;

public class BoundArrayLiteralExpression extends BoundExpression {
    @Getter
    private final ArrayTypeSymbol elementType;
    @Getter
    private final List<BoundExpression> elements;

    public BoundArrayLiteralExpression(SyntaxNode syntax, ArrayTypeSymbol type, List<BoundExpression> elements) {
        super(syntax);
        this.elementType = type;
        this.elements = elements;
    }

    @Override
    public TypeSymbol getType() {
        return elementType;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.ARRAY_LITERAL_EXPRESSION;
    }
}
