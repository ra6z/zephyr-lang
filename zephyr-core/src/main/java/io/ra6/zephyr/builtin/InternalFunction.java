package io.ra6.zephyr.builtin;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeFactory;
import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.statements.BoundBlockStatement;
import io.ra6.zephyr.codeanalysis.symbols.FunctionSymbol;
import io.ra6.zephyr.codeanalysis.symbols.ParameterSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.symbols.VariableSymbol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class InternalFunction extends InternalFunctionBase {
    private final String functionName;
    private final boolean isShared;
    private final Visibility visibility;
    private final List<ParameterSymbol> parameters;
    private final TypeSymbol returnType;
    private final IFunction functionBody;

    public FunctionSymbol getFunctionSymbol() {
        return new FunctionSymbol(functionName, isShared, visibility, parameters, returnType);
    }

    public BoundBlockStatement bindBody() {
        List<BoundExpression> args = new ArrayList<>();

        for (ParameterSymbol parameter : parameters) {
            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            args.add(BoundNodeFactory.createVariableExpression(null, variable));
        }
        BoundExpression boundExpression = BoundNodeFactory.createInternalFunctionExpression(null, this, args);

        return BoundNodeFactory.createBlockStatement(null,
                BoundNodeFactory.createReturnStatement(null, boundExpression)
        );
    }

    @Override
    public IFunction getFunctionBody() {
        return functionBody;
    }

    @Override
    public int getArity() {
        return parameters.size();
    }
}
