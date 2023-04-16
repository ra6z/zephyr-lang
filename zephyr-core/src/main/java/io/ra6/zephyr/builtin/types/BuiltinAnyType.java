package io.ra6.zephyr.builtin.types;

import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.codeanalysis.symbols.ParameterSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.runtime.TypeInstance;

import java.util.List;

import static io.ra6.zephyr.builtin.IFunctionBase.PARAM_THIS;

public class BuiltinAnyType extends BuiltinType {

    private final BoundTypeScope typeScope = new BoundTypeScope(null, Types.ANY);

    private final InternalFunction toString = new InternalFunction("toString", false, Visibility.PUBLIC, List.of(), Types.STRING, args -> {
        Object value = args.get(PARAM_THIS);
        if (value instanceof String) {
            return value;
        }
        if (value instanceof Integer) {
            return value.toString();
        }
        if (value instanceof TypeInstance) {
            return ((TypeInstance) value).getRuntimeType().getName();
        }
        return value.toString();
    });

    private final InternalFunction equals = new InternalFunction("equals", false, Visibility.PUBLIC, List.of(new ParameterSymbol("other", Types.ANY)), Types.BOOL, args -> {
        Object value = args.get(PARAM_THIS);
        Object other = args.get("other");

        if (value instanceof TypeInstance valI) {
            if (!(other instanceof TypeInstance valO)) {
                return false;
            }
            return valI.getRuntimeType().getType().equals(valO.getRuntimeType().getType());
        }

        if (value instanceof String valI) {
            if (!(other instanceof String valO)) {
                return false;
            }
            return valI.equals(valO);
        }

        if (value instanceof Integer valI) {
            if (!(other instanceof Integer valO)) {
                return false;
            }
            return valI.equals(valO);
        }

        if (value instanceof Boolean valI) {
            if (!(other instanceof Boolean valO)) {
                return false;
            }
            return valI.equals(valO);
        }

        if (value instanceof Double valI) {
            if (!(other instanceof Double valO)) {
                return false;
            }
            return valI.equals(valO);
        }

        return value.equals(other);
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
        typeScope.declareFunction(equals);
    }

    @Override
    protected void defineFunctions() {
        typeScope.defineFunction(toString);
        typeScope.defineFunction(equals);
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
        return Types.ANY;
    }

    @Override
    public BoundTypeScope getTypeScope() {
        return typeScope;
    }
}
