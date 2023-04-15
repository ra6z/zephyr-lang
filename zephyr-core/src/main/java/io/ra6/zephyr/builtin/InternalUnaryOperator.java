package io.ra6.zephyr.builtin;

import io.ra6.zephyr.codeanalysis.binding.BoundNodeFactory;
import io.ra6.zephyr.codeanalysis.binding.statements.BoundBlockStatement;
import io.ra6.zephyr.codeanalysis.symbols.ParameterSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.symbols.UnaryOperatorSymbol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class InternalUnaryOperator implements IFunctionBase {
    private final String operator;
    private final TypeSymbol resultType;
    private final ICallable functionBody;

    public UnaryOperatorSymbol getUnaryOperatorSymbol() {
        return new UnaryOperatorSymbol(operator, resultType);
    }

    public BoundBlockStatement bindBody() {
        return BoundNodeFactory.createBlockStatement(null,
                BoundNodeFactory.createReturnStatement(null,
                        BoundNodeFactory.createInternalFunctionExpression(null, this, List.of(
                                //        selfExpression
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
        return 0;
    }

    @Override
    public List<ParameterSymbol> getParameters() {
        return List.of();
    }
}
