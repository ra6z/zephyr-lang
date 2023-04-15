package io.ra6.zephyr.builtin;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeFactory;
import io.ra6.zephyr.codeanalysis.binding.statements.BoundBlockStatement;
import io.ra6.zephyr.codeanalysis.symbols.BinaryOperatorSymbol;
import io.ra6.zephyr.codeanalysis.symbols.ParameterSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.symbols.VariableSymbol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class InternalBinaryOperator implements IFunctionBase {
    private final String operator;
    private final TypeSymbol otherType;
    private final TypeSymbol resultType;
    private final ICallable functionBody;

    public BinaryOperatorSymbol getBinaryOperatorSymbol() {
        return new BinaryOperatorSymbol(operator, PARAM_OTHER, otherType, resultType);
    }

    public BoundBlockStatement bindBody() {
        VariableSymbol otherVariable = new VariableSymbol(PARAM_OTHER, true, otherType);

        BoundExpression otherExpression = BoundNodeFactory.createVariableExpression(null, otherVariable);

        return BoundNodeFactory.createBlockStatement(null,
                BoundNodeFactory.createReturnStatement(null,
                        BoundNodeFactory.createInternalFunctionExpression(null, this, List.of(
                                otherExpression
                        ))
                )
        );
    }

    @Override
    public ICallable getFunctionBody() {
        return functionBody;
    }

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public List<ParameterSymbol> getParameters() {
        return List.of(
                new ParameterSymbol(PARAM_OTHER, otherType)
        );
    }
}
