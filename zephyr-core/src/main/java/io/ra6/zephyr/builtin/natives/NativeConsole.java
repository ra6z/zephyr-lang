package io.ra6.zephyr.builtin.natives;

import io.ra6.zephyr.builtin.BuiltinTypes;
import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.codeanalysis.symbols.ParameterSymbol;

import java.util.List;

public class NativeConsole extends NativeType {
    private final BoundTypeScope typeScope = new BoundTypeScope(null, getTypeSymbol());
    private final InternalFunction logString = new InternalFunction("logString", true, Visibility.PUBLIC, List.of(new ParameterSymbol("message", BuiltinTypes.STRING)), BuiltinTypes.VOID, args -> {
        String str = (String) args.get("message");
        System.out.println(str);
        return null;
    });

    @Override
    public String getNativeName() {
        return "NativeConsole";
    }

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
        typeScope.declareFunction(logString.getFunctionSymbol());
    }

    @Override
    protected void defineFunctions() {
        typeScope.defineFunction(logString.getFunctionSymbol(), logString.bindBody());
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
    public BoundTypeScope getTypeScope() {
        return typeScope;
    }
}
