package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.symbols.VariableSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

public class BoundVariableExpression extends BoundExpression {
    @Getter
    private final VariableSymbol variable;

    public BoundVariableExpression(SyntaxNode syntax, VariableSymbol variable) {
        super(syntax);
        this.variable = variable;
    }

    @Override
    public TypeSymbol getType() {
        return variable.getType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.VARIABLE_EXPRESSION;
    }
}
