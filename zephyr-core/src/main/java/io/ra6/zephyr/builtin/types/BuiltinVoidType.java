package io.ra6.zephyr.builtin.types;

import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.builtin.BuiltinTypes;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;

public class BuiltinVoidType extends BuiltinType {

    private final BoundTypeScope typeScope = new BoundTypeScope(null, BuiltinTypes.VOID);

    @Override
    protected void declareFields() {

    }

    @Override
    protected void declareConstructors() {

    }

    @Override
    protected void declareFunctions() {

    }

    @Override
    protected void declareBinaryOperators() {

    }

    @Override
    protected void declareUnaryOperators() {

    }

    @Override
    protected void defineFields() {

    }

    @Override
    protected void defineConstructors() {

    }

    @Override
    protected void defineFunctions() {

    }

    @Override
    protected void defineBinaryOperators() {

    }

    @Override
    protected void defineUnaryOperators() {

    }

    @Override
    public TypeSymbol getTypeSymbol() {
        return BuiltinTypes.VOID;
    }

    @Override
    public BoundTypeScope getTypeScope() {
        return typeScope;
    }
}
