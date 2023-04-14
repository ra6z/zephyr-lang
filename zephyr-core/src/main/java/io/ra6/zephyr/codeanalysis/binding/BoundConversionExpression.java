package io.ra6.zephyr.codeanalysis.binding;

import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

public class BoundConversionExpression extends BoundExpression {
    @Getter
    private final TypeSymbol boundGenericType;
    @Getter
    private final TypeSymbol genericType;
    @Getter
    private final BoundExpression expression;

    protected BoundConversionExpression(SyntaxNode syntax, TypeSymbol boundGenericType, TypeSymbol genericType, BoundExpression expression) {
        super(syntax);
        this.boundGenericType = boundGenericType;
        this.genericType = genericType;
        this.expression = expression;
    }

    @Override
    public TypeSymbol getType() {
        return boundGenericType;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.CONVERSION_EXPRESSION;
    }
}
