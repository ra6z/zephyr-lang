package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.FieldSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

public class BoundFieldAccessExpression extends BoundExpression {
    @Getter
    private final BoundExpression target;
    @Getter
    private final FieldSymbol field;

    public BoundFieldAccessExpression(SyntaxNode syntax, BoundExpression target, FieldSymbol field) {
        super(syntax);
        this.target = target;
        this.field = field;
    }

    @Override
    public TypeSymbol getType() {
        return field.getType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.FIELD_ACCESS_EXPRESSION;
    }
}
