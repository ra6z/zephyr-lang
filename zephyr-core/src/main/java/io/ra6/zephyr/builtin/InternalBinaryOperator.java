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
public class InternalBinaryOperator extends InternalFunctionBase {
    private final String operator;
    private final TypeSymbol otherType;
    private final TypeSymbol resultType;
    private final IFunction functionBody;

    public BinaryOperatorSymbol getBinaryOperatorSymbol() {
        return new BinaryOperatorSymbol(operator, "other", otherType, resultType);
    }

    public BoundBlockStatement bindBody() {
//        VariableSymbol selfVariable = new VariableSymbol("self", true, selfType);
        VariableSymbol otherVariable = new VariableSymbol("other", true, otherType);

//        BoundExpression selfExpression = BoundNodeFactory.createVariableExpression(null, selfVariable);
        BoundExpression otherExpression = BoundNodeFactory.createVariableExpression(null, otherVariable);

        return BoundNodeFactory.createBlockStatement(null,
                BoundNodeFactory.createReturnStatement(null,
                        BoundNodeFactory.createInternalFunctionExpression(null, this, List.of(
                                //selfExpression,
                                otherExpression
                        ))
                )
        );
    }

    @Override
    public IFunction getFunctionBody() {
        return functionBody;
    }

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public List<ParameterSymbol> getParameters() {
        return List.of(
                //new ParameterSymbol("self", selfType),
                new ParameterSymbol("other", otherType)
        );
    }
}
