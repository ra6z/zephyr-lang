package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.FunctionSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

import java.util.List;

public class BoundFunctionCallExpression extends BoundExpression {
    @Getter
    private final BoundExpression callee;
    @Getter
    private final FunctionSymbol function;
    @Getter
    private final List<BoundExpression> arguments;

    public BoundFunctionCallExpression(SyntaxNode syntax, BoundExpression callee, FunctionSymbol function, List<BoundExpression> arguments) {
        super(syntax);
        this.callee = callee;
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public TypeSymbol getType() {
        return function.getType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.FUNCTION_CALL_EXPRESSION;
    }
}
