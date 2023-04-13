package io.ra6.zephyr.builtin.types;

import io.ra6.zephyr.codeanalysis.binding.BoundNodeFactory;
import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.builtin.BuiltinTypes;
import io.ra6.zephyr.builtin.InternalBinaryOperator;
import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.builtin.InternalUnaryOperator;
import io.ra6.zephyr.codeanalysis.symbols.*;

import java.util.List;

public class BuiltinIntType extends BuiltinType {
    private final BoundTypeScope typeScope = new BoundTypeScope(null, getTypeSymbol());

    private final FieldSymbol max = createPubSharedField("max", BuiltinTypes.INT, true);
    private final FieldSymbol min = createPubSharedField("min", BuiltinTypes.INT, true);
    private final FieldSymbol size = createPubSharedField("size", BuiltinTypes.INT, true);

    private final InternalFunction fromDouble = new InternalFunction("fromDouble", true, Visibility.PUBLIC, List.of(new ParameterSymbol("other", BuiltinTypes.DOUBLE)), BuiltinTypes.INT, args -> {
        double otherValue = (double) args.get("other");
        return (int) otherValue;
    });

    private final InternalFunction toString = new InternalFunction("toString", false, Visibility.PUBLIC, List.of(), BuiltinTypes.STRING, args -> {
        int thisValue = (int) args.get("this");
        return Integer.toString(thisValue);
    });

    @Override
    protected void declareFields() {
        typeScope.declareField(max);
        typeScope.declareField(min);
        typeScope.declareField(size);
    }

    @Override
    protected void defineFields() {
        typeScope.defineField(max, BoundNodeFactory.createIntLiteral(null, Integer.MAX_VALUE));
        typeScope.defineField(min, BoundNodeFactory.createIntLiteral(null, Integer.MIN_VALUE));
        typeScope.defineField(size, BoundNodeFactory.createIntLiteral(null, Integer.SIZE));
    }

    @Override
    protected void declareConstructors() {

    }

    @Override
    protected void defineConstructors() {

    }

    @Override
    protected void declareFunctions() {
        typeScope.declareFunction(fromDouble.getFunctionSymbol());
        typeScope.declareFunction(toString.getFunctionSymbol());
    }

    @Override
    protected void defineFunctions() {
        typeScope.defineFunction(fromDouble.getFunctionSymbol(), fromDouble.bindBody());
        typeScope.defineFunction(toString.getFunctionSymbol(), toString.bindBody());
    }

    @Override
    protected void declareBinaryOperators() {
        typeScope.declareBinaryOperator(createBinaryOperator("+", BuiltinTypes.INT, BuiltinTypes.INT));
        typeScope.declareBinaryOperator(createBinaryOperator("-", BuiltinTypes.INT, BuiltinTypes.INT));
        typeScope.declareBinaryOperator(createBinaryOperator("*", BuiltinTypes.INT, BuiltinTypes.INT));
        typeScope.declareBinaryOperator(createBinaryOperator("/", BuiltinTypes.INT, BuiltinTypes.INT));
        typeScope.declareBinaryOperator(createBinaryOperator("%", BuiltinTypes.INT, BuiltinTypes.INT));

        typeScope.declareBinaryOperator(createBinaryOperator("&", BuiltinTypes.INT, BuiltinTypes.INT));
        typeScope.declareBinaryOperator(createBinaryOperator("|", BuiltinTypes.INT, BuiltinTypes.INT));
        typeScope.declareBinaryOperator(createBinaryOperator("^", BuiltinTypes.INT, BuiltinTypes.INT));

        typeScope.declareBinaryOperator(createBinaryOperator("==", BuiltinTypes.INT, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("!=", BuiltinTypes.INT, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">", BuiltinTypes.INT, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<", BuiltinTypes.INT, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">=", BuiltinTypes.INT, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<=", BuiltinTypes.INT, BuiltinTypes.BOOL));

        typeScope.declareBinaryOperator(createBinaryOperator("+", BuiltinTypes.DOUBLE, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("-", BuiltinTypes.DOUBLE, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("*", BuiltinTypes.DOUBLE, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("/", BuiltinTypes.DOUBLE, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("%", BuiltinTypes.DOUBLE, BuiltinTypes.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("==", BuiltinTypes.DOUBLE, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("!=", BuiltinTypes.DOUBLE, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">", BuiltinTypes.DOUBLE, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<", BuiltinTypes.DOUBLE, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">=", BuiltinTypes.DOUBLE, BuiltinTypes.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<=", BuiltinTypes.DOUBLE, BuiltinTypes.BOOL));
    }


    @Override
    protected void defineBinaryOperators() {
        for (BinaryOperatorSymbol symbol : typeScope.getDeclaredBinaryOperators()) {
            TypeSymbol otherType = symbol.getOtherType();

            InternalBinaryOperator ibo = new InternalBinaryOperator(symbol.getName(), symbol.getOtherType(), symbol.getReturnType(), args -> {
                int selfValue = (int) args.get("this");

                if (otherType == BuiltinTypes.INT) {
                    int otherValue = (int) args.get("other");
                    return switch (symbol.getName()) {
                        case "+" -> selfValue + otherValue;
                        case "-" -> selfValue - otherValue;
                        case "*" -> selfValue * otherValue;
                        case "/" -> selfValue / otherValue;
                        case "%" -> selfValue % otherValue;

                        case "&" -> selfValue & otherValue;
                        case "|" -> selfValue | otherValue;
                        case "^" -> selfValue ^ otherValue;

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
        typeScope.declareUnaryOperator(createUnaryOperator("-", BuiltinTypes.INT));
        typeScope.declareUnaryOperator(createUnaryOperator("+", BuiltinTypes.INT));
        typeScope.declareUnaryOperator(createUnaryOperator("~", BuiltinTypes.INT));
    }


    @Override
    protected void defineUnaryOperators() {
        for (UnaryOperatorSymbol symbol : typeScope.getDeclaredUnaryOperators()) {
            InternalUnaryOperator iuo = new InternalUnaryOperator(symbol.getName(), symbol.getReturnType(), args -> {
                int selfValue = (int) args.get("this");

                return switch (symbol.getName()) {
                    case "-" -> -selfValue;
                    case "+" -> +selfValue;
                    case "~" -> ~selfValue;
                    default -> throw new RuntimeException("Unexpected unary operator: " + symbol.getName());
                };
            });

            typeScope.defineUnaryOperator(symbol, iuo.bindBody());
        }
    }

    @Override
    public TypeSymbol getTypeSymbol() {
        return BuiltinTypes.INT;
    }

    @Override
    public BoundTypeScope getTypeScope() {
        return typeScope;
    }
}
