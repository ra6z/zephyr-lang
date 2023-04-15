package io.ra6.zephyr.builtin.types;

import io.ra6.zephyr.codeanalysis.binding.BoundNodeFactory;
import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.builtin.InternalBinaryOperator;
import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.builtin.InternalUnaryOperator;
import io.ra6.zephyr.codeanalysis.symbols.*;

import java.util.List;

import static io.ra6.zephyr.builtin.IFunctionBase.PARAM_OTHER;
import static io.ra6.zephyr.builtin.IFunctionBase.PARAM_THIS;

public class BuiltinIntType extends BuiltinType {

    private final BoundTypeScope typeScope = new BoundTypeScope(null, Types.INT);
    private final FieldSymbol max = createPubSharedField("MAX", Types.INT, true);
    private final FieldSymbol min = createPubSharedField("MIN", Types.INT, true);
    private final FieldSymbol size = createPubSharedField("SIZE", Types.INT, true);

    private final InternalFunction fromDouble = new InternalFunction("fromDouble", true, Visibility.PUBLIC, List.of(new ParameterSymbol(PARAM_OTHER, Types.DOUBLE)), Types.INT, args -> {
        double otherValue = (double) args.get(PARAM_OTHER);
        return (int) otherValue;
    });

    private final InternalFunction toString = new InternalFunction("toString", false, Visibility.PUBLIC, List.of(), Types.STRING, args -> {
        int thisValue = (int) args.get(PARAM_THIS);
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
        typeScope.declareFunction(fromDouble);
        typeScope.declareFunction(toString);
    }

    @Override
    protected void defineFunctions() {
        typeScope.defineFunction(fromDouble);
        typeScope.defineFunction(toString);
    }

    @Override
    protected void declareBinaryOperators() {
        typeScope.declareBinaryOperator(createBinaryOperator("+", Types.INT, Types.INT));
        typeScope.declareBinaryOperator(createBinaryOperator("-", Types.INT, Types.INT));
        typeScope.declareBinaryOperator(createBinaryOperator("*", Types.INT, Types.INT));
        typeScope.declareBinaryOperator(createBinaryOperator("/", Types.INT, Types.INT));
        typeScope.declareBinaryOperator(createBinaryOperator("%", Types.INT, Types.INT));

        typeScope.declareBinaryOperator(createBinaryOperator("&", Types.INT, Types.INT));
        typeScope.declareBinaryOperator(createBinaryOperator("|", Types.INT, Types.INT));
        typeScope.declareBinaryOperator(createBinaryOperator("^", Types.INT, Types.INT));

        typeScope.declareBinaryOperator(createBinaryOperator("==", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("!=", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">=", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<=", Types.INT, Types.BOOL));

        typeScope.declareBinaryOperator(createBinaryOperator("+", Types.DOUBLE, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("-", Types.DOUBLE, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("*", Types.DOUBLE, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("/", Types.DOUBLE, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("%", Types.DOUBLE, Types.DOUBLE));
        typeScope.declareBinaryOperator(createBinaryOperator("==", Types.DOUBLE, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("!=", Types.DOUBLE, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">", Types.DOUBLE, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<", Types.DOUBLE, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">=", Types.DOUBLE, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<=", Types.DOUBLE, Types.BOOL));
    }


    @Override
    protected void defineBinaryOperators() {
        for (BinaryOperatorSymbol symbol : typeScope.getDeclaredBinaryOperators()) {
            TypeSymbol otherType = symbol.getOtherType();

            InternalBinaryOperator ibo = new InternalBinaryOperator(symbol.getName(), symbol.getOtherType(), symbol.getReturnType(), args -> {
                int selfValue = (int) args.get(PARAM_THIS);

                if (otherType == Types.INT) {
                    int otherValue = (int) args.get(PARAM_OTHER);
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
        typeScope.declareUnaryOperator(createUnaryOperator("-", Types.INT));
        typeScope.declareUnaryOperator(createUnaryOperator("+", Types.INT));
        typeScope.declareUnaryOperator(createUnaryOperator("~", Types.INT));
    }


    @Override
    protected void defineUnaryOperators() {
        for (UnaryOperatorSymbol symbol : typeScope.getDeclaredUnaryOperators()) {
            InternalUnaryOperator iuo = new InternalUnaryOperator(symbol.getName(), symbol.getReturnType(), args -> {
                int selfValue = (int) args.get(PARAM_THIS);

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
        return Types.INT;
    }

    @Override
    public BoundTypeScope getTypeScope() {
        return typeScope;
    }
}
