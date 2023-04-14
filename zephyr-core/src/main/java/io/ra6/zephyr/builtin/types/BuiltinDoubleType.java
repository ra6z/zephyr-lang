package io.ra6.zephyr.builtin.types;

import io.ra6.zephyr.codeanalysis.binding.BoundNodeFactory;
import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.builtin.BuiltinTypes;
import io.ra6.zephyr.builtin.InternalBinaryOperator;
import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.codeanalysis.symbols.BinaryOperatorSymbol;
import io.ra6.zephyr.codeanalysis.symbols.FieldSymbol;
import io.ra6.zephyr.codeanalysis.symbols.ParameterSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;

import java.util.List;

public class BuiltinDoubleType extends BuiltinType {

    private final BoundTypeScope typeScope = new BoundTypeScope(null, BuiltinTypes.DOUBLE);

    private final FieldSymbol min = createPubSharedField("min", BuiltinTypes.DOUBLE, true);
    private final FieldSymbol max = createPubSharedField("max", BuiltinTypes.DOUBLE, true);
    private final FieldSymbol size = createPubSharedField("size", BuiltinTypes.DOUBLE, true);

    private final InternalFunction fromInt = new InternalFunction("fromInt", true, Visibility.PUBLIC, List.of(new ParameterSymbol("other", BuiltinTypes.INT)), BuiltinTypes.DOUBLE, args -> {
        int otherValue = (int) args.get("other");
        return (double) otherValue;
    });

    private final InternalFunction toString = new InternalFunction("toString", false, Visibility.PUBLIC, List.of(new ParameterSymbol("other", BuiltinTypes.DOUBLE)), BuiltinTypes.STRING, args -> {
        double otherValue = (double) args.get("this");
        return Double.toString(otherValue);
    });

    @Override
    protected void declareFields() {
        typeScope.declareField(min);
        typeScope.declareField(max);
        typeScope.declareField(size);
    }

    @Override
    protected void defineFields() {
        typeScope.defineField(min, BoundNodeFactory.createDoubleLiteral(null, Double.MIN_VALUE));
        typeScope.defineField(max, BoundNodeFactory.createDoubleLiteral(null, Double.MAX_VALUE));
        typeScope.defineField(size, BoundNodeFactory.createDoubleLiteral(null, Double.SIZE));
    }

    @Override
    protected void declareConstructors() {

    }

    @Override
    protected void defineConstructors() {

    }

    @Override
    protected void declareFunctions() {
        typeScope.declareFunction(fromInt.getFunctionSymbol());
        typeScope.declareFunction(toString.getFunctionSymbol());
    }

    @Override
    protected void defineFunctions() {
        typeScope.defineFunction(fromInt.getFunctionSymbol(), fromInt.bindBody());
        typeScope.defineFunction(toString.getFunctionSymbol(), toString.bindBody());
    }

    @Override
    protected void declareBinaryOperators() {
        typeScope.declareBinaryOperator(createBinaryOperator("+", BuiltinTypes.DOUBLE, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("-", BuiltinTypes.DOUBLE, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("*", BuiltinTypes.DOUBLE, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("/", BuiltinTypes.DOUBLE, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("%", BuiltinTypes.DOUBLE, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("==", BuiltinTypes.DOUBLE, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("!=", BuiltinTypes.DOUBLE, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<", BuiltinTypes.DOUBLE, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">", BuiltinTypes.DOUBLE, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<=", BuiltinTypes.DOUBLE, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">=", BuiltinTypes.DOUBLE, BuiltinTypes.BOOL));

        typeScope.declareBinaryOperator(createBinaryOperator("+", BuiltinTypes.INT, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("-", BuiltinTypes.INT, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("*", BuiltinTypes.INT, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("/", BuiltinTypes.INT, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("%", BuiltinTypes.INT, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("==", BuiltinTypes.INT, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("!=", BuiltinTypes.INT, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<", BuiltinTypes.INT, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">", BuiltinTypes.INT, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<=", BuiltinTypes.INT, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">=", BuiltinTypes.INT, BuiltinTypes.BOOL));
    }

    @Override
    protected void defineBinaryOperators() {
        for (BinaryOperatorSymbol symbol : typeScope.getDeclaredBinaryOperators()) {
            TypeSymbol otherType = symbol.getOtherType();

            InternalBinaryOperator ibo = new InternalBinaryOperator(symbol.getName(), symbol.getOtherType(), symbol.getReturnType(), args -> {
                double selfValue = (double) args.get("this");

                if (otherType == BuiltinTypes.INT) {
                    int otherValue = (int) args.get("other");

                    return switch (symbol.getName()) {
                        case "+" -> selfValue + otherValue;
                        case "-" -> selfValue - otherValue;
                        case "*" -> selfValue * otherValue;
                        case "/" -> selfValue / otherValue;
                        case "%" -> selfValue % otherValue;

                        case "==" -> selfValue == otherValue;
                        case "!=" -> selfValue != otherValue;
                        case ">" -> selfValue > otherValue;
                        case "<" -> selfValue < otherValue;
                        case ">=" -> selfValue >= otherValue;
                        case "<=" -> selfValue <= otherValue;
                        default -> throw new RuntimeException("Unexpected binary operator: " + symbol.getName());
                    };
                } else if (otherType == BuiltinTypes.DOUBLE) {
                    double otherValue = (double) args.get("other");
                    return switch (symbol.getName()) {
                        case "+" -> selfValue + otherValue;
                        case "-" -> selfValue - otherValue;
                        case "*" -> selfValue * otherValue;
                        case "/" -> selfValue / otherValue;
                        case "%" -> selfValue % otherValue;

                        case "==" -> selfValue == otherValue;
                        case "!=" -> selfValue != otherValue;
                        case ">" -> selfValue > otherValue;
                        case "<" -> selfValue < otherValue;
                        case ">=" -> selfValue >= otherValue;
                        case "<=" -> selfValue <= otherValue;
                        default -> throw new RuntimeException("Unexpected binary operator: " + symbol.getName());
                    };
                }

                throw new RuntimeException("Unexpected binary operator: " + symbol.getName());
            });

            typeScope.defineBinaryOperator(symbol, ibo.bindBody());
        }
    }

    @Override
    protected void declareUnaryOperators() {

    }

    @Override
    protected void defineUnaryOperators() {

    }

    @Override
    public TypeSymbol getTypeSymbol() {
        return BuiltinTypes.DOUBLE;
    }

    @Override
    public BoundTypeScope getTypeScope() {
        return typeScope;
    }
}
