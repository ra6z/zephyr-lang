package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import io.ra6.zephyr.codeanalysis.syntax.expressions.LiteralExpressionSyntax;
import lombok.Getter;

public class BoundLiteralExpression extends BoundExpression {
    @Getter
    private final Object value;
    private final TypeSymbol type;

    public BoundLiteralExpression(SyntaxNode syntax, Object value, TypeSymbol type) {
        super(syntax);
        this.value = value;
        this.type = type;
    }

    @Override
    public TypeSymbol getType() {
        return type;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.LITERAL_EXPRESSION;
    }
}
