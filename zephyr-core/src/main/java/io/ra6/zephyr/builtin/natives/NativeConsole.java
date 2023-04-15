package io.ra6.zephyr.builtin.natives;

import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.codeanalysis.symbols.ParameterSymbol;

import java.util.List;

public class NativeConsole extends NativeType {
    private final BoundTypeScope typeScope = new BoundTypeScope(null, getTypeSymbol());
    private final InternalFunction printlnString = new InternalFunction("printlnString", true, Visibility.PUBLIC, List.of(new ParameterSymbol("message", Types.STRING)), Types.VOID, args -> {
        String str = (String) args.get("message");
        System.out.println(str);
        return null;
    });

    private final InternalFunction printString = new InternalFunction("printString", true, Visibility.PUBLIC, List.of(new ParameterSymbol("message", Types.STRING)), Types.VOID, args -> {
        String str = (String) args.get("message");
        System.out.print(str);
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
        typeScope.declareFunction(printlnString);
        typeScope.declareFunction(printString);
    }

    @Override
    protected void defineFunctions() {
        typeScope.defineFunction(printlnString);
        typeScope.defineFunction(printString);
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
