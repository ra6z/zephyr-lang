package io.ra6.zephyr.builtin.types;

import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.runtime.TypeInstance;

import java.util.List;

import static io.ra6.zephyr.builtin.IFunctionBase.PARAM_THIS;

public class BuiltinErrorType extends BuiltinType {

    private final BoundTypeScope typeScope = new BoundTypeScope(null, Types.ERROR);

    private final InternalFunction toString = new InternalFunction("toString", false, Visibility.PUBLIC, List.of(), Types.STRING, args -> {
        return "Error";
    });

    @Override
    protected void declareFields() {

    }

    @Override
    protected void defineFields() {

    }

    @Override
    protected void declareConstructors() {

    }

    @Override
    protected void defineConstructors() {

    }

    @Override
    protected void declareFunctions() {
        typeScope.declareFunction(toString);
    }

    @Override
    protected void defineFunctions() {
        typeScope.defineFunction(toString);
    }

    @Override
    protected void declareBinaryOperators() {

    }

    @Override
    protected void defineBinaryOperators() {

    }

    @Override
    protected void declareUnaryOperators() {

    }

    @Override
    protected void defineUnaryOperators() {

    }

    @Override
    public TypeSymbol getTypeSymbol() {
        return Types.ERROR;
    }

    @Override
    public BoundTypeScope getTypeScope() {
        return typeScope;
    }
}
