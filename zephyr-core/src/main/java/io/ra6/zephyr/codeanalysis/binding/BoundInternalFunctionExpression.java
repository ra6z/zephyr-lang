package io.ra6.zephyr.codeanalysis.binding;

import io.ra6.zephyr.builtin.InternalFunctionBase;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

import java.util.List;

public class BoundInternalFunctionExpression extends BoundExpression {
    @Getter
    private final InternalFunctionBase function;
    @Getter
    private final List<BoundExpression> arguments;

    public BoundInternalFunctionExpression(SyntaxNode node, InternalFunctionBase function, List<BoundExpression> arguments) {
        super(node);
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public TypeSymbol getType() {
        return null;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.INTERNAL_FUNCTION_EXPRESSION;
    }
}
