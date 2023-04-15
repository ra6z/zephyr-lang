package io.ra6.zephyr.builtin.types;

import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.codeanalysis.symbols.BinaryOperatorSymbol;
import io.ra6.zephyr.codeanalysis.symbols.FieldSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.symbols.UnaryOperatorSymbol;

import static io.ra6.zephyr.builtin.IFunctionBase.PARAM_OTHER;

public abstract class BuiltinType {
    protected abstract void declareFields();

    protected abstract void defineFields();

    protected abstract void declareConstructors();

    protected abstract void defineConstructors();

    protected abstract void declareFunctions();

    protected abstract void defineFunctions();

    protected abstract void declareBinaryOperators();

    protected abstract void defineBinaryOperators();

    protected abstract void declareUnaryOperators();

    protected abstract void defineUnaryOperators();

    public final void declareAll() {
        declareFields();
        declareConstructors();
        declareFunctions();
        declareBinaryOperators();
        declareUnaryOperators();

        getTypeSymbol().setFieldsAndFunctions(getTypeScope().getDeclaredFieldsAndFunctions());
        getTypeSymbol().setConstructors(getTypeScope().getDeclaredConstructors());
        getTypeSymbol().setBinaryOperators(getTypeScope().getDeclaredBinaryOperators());
        getTypeSymbol().setUnaryOperators(getTypeScope().getDeclaredUnaryOperators());
    }

    public final void defineAll() {
        defineFields();
        defineConstructors();
        defineFunctions();
        defineBinaryOperators();
        defineUnaryOperators();
    }

    public abstract TypeSymbol getTypeSymbol();

    public abstract BoundTypeScope getTypeScope();

    protected FieldSymbol createPubSharedField(String name, TypeSymbol type, boolean readonly) {
        return new FieldSymbol(name, readonly, true, Visibility.PUBLIC, type);
    }

    protected BinaryOperatorSymbol createBinaryOperator(String operator, TypeSymbol otherType, TypeSymbol returnType) {
        return new BinaryOperatorSymbol(operator, PARAM_OTHER, otherType, returnType);
    }

    protected UnaryOperatorSymbol createUnaryOperator(String operator, TypeSymbol returnType) {
        return new UnaryOperatorSymbol(operator, returnType);
    }
}
