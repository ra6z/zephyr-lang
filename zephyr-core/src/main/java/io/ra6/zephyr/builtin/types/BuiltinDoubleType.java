package io.ra6.zephyr.builtin.types;

import io.ra6.zephyr.builtin.InternalBinaryOperator;
import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeFactory;
import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.codeanalysis.symbols.BinaryOperatorSymbol;
import io.ra6.zephyr.codeanalysis.symbols.FieldSymbol;
import io.ra6.zephyr.codeanalysis.symbols.ParameterSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;

import java.util.List;

import static io.ra6.zephyr.builtin.IFunctionBase.PARAM_OTHER;
import static io.ra6.zephyr.builtin.IFunctionBase.PARAM_THIS;

public class BuiltinDoubleType extends BuiltinType {

    private final BoundTypeScope typeScope = new BoundTypeScope(null, Types.DOUBLE);

    private final FieldSymbol min = createPubSharedField("MIN", Types.DOUBLE, true);
    private final FieldSymbol max = createPubSharedField("MAX", Types.DOUBLE, true);
    private final FieldSymbol size = createPubSharedField("SIZE", Types.DOUBLE, true);

    private final InternalFunction fromInt = new InternalFunction("fromInt", true, Visibility.PUBLIC, List.of(new ParameterSymbol(PARAM_OTHER, Types.INT)), Types.DOUBLE, args -> {
        int otherValue = (int) args.get(PARAM_OTHER);
        return (double) otherValue;
    });

    private final InternalFunction toString = new InternalFunction("toString", false, Visibility.PUBLIC, List.of(), Types.STRING, args -> {
        double otherValue = (double) args.get(PARAM_THIS);
        return Double.toString(otherValue);
    });

    private final InternalFunction equals = new InternalFunction("equals", false, Visibility.PUBLIC, List.of(new ParameterSymbol("other", Types.ANY)), Types.BOOL, args -> {
        double selfValue = (double) args.get(PARAM_THIS);

        if (!(args.get(PARAM_OTHER) instanceof Double)) {
            return false;
        }

        return selfValue == (double) args.get(PARAM_OTHER);
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
        typeScope.declareFunction(fromInt);
        typeScope.declareFunction(toString);
        typeScope.declareFunction(equals);
    }

    @Override
    protected void defineFunctions() {
        typeScope.defineFunction(fromInt);
        typeScope.defineFunction(toString);
        typeScope.defineFunction(equals);
    }

    @Override
    protected void declareBinaryOperators() {
        typeScope.declareBinaryOperator(createBinaryOperator("+", Types.DOUBLE, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("-", Types.DOUBLE, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("*", Types.DOUBLE, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("/", Types.DOUBLE, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("%", Types.DOUBLE, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("==", Types.DOUBLE, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("!=", Types.DOUBLE, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<", Types.DOUBLE, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">", Types.DOUBLE, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<=", Types.DOUBLE, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">=", Types.DOUBLE, Types.BOOL));

        typeScope.declareBinaryOperator(createBinaryOperator("+", Types.INT, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("-", Types.INT, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("*", Types.INT, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("/", Types.INT, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("%", Types.INT, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("==", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("!=", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<=", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">=", Types.INT, Types.BOOL));
    }

    @Override
    protected void defineBinaryOperators() {
        for (BinaryOperatorSymbol symbol : typeScope.getDeclaredBinaryOperators()) {
            TypeSymbol otherType = symbol.getOtherType();

            InternalBinaryOperator ibo = new InternalBinaryOperator(symbol.getName(), symbol.getOtherType(), symbol.getReturnType(), args -> {
                double selfValue = (double) args.get(PARAM_THIS);

                if (otherType == Types.INT) {
                    int otherValue = (int) args.get(PARAM_OTHER);

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
                } else if (otherType == Types.DOUBLE) {
                    double otherValue = (double) args.get(PARAM_OTHER);
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
        return Types.DOUBLE;
    }

    @Override
    public BoundTypeScope getTypeScope() {
        return typeScope;
    }
}
