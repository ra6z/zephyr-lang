package io.ra6.zephyr.builtin.types;

import io.ra6.zephyr.builtin.InternalBinaryOperator;
import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeFactory;
import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.codeanalysis.symbols.BinaryOperatorSymbol;
import io.ra6.zephyr.codeanalysis.symbols.FieldSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;

import java.util.List;

import static io.ra6.zephyr.builtin.IFunctionBase.PARAM_OTHER;
import static io.ra6.zephyr.builtin.IFunctionBase.PARAM_THIS;

public class BuiltinCharType extends BuiltinType {
    private final BoundTypeScope typeScope = new BoundTypeScope(null, Types.CHAR);

    private final FieldSymbol min = createPubSharedField("MIN", Types.CHAR, true);
    private final FieldSymbol max = createPubSharedField("MAX", Types.CHAR, true);

    private final InternalFunction toChar = new InternalFunction("toChar", false, Visibility.PUBLIC, List.of(), Types.CHAR, args -> (char) ((int) args.get(PARAM_THIS)));
    private final InternalFunction toString = new InternalFunction("toString", false, Visibility.PUBLIC, List.of(), Types.STRING, args -> String.valueOf((char) args.get(PARAM_THIS)));

    @Override
    protected void declareFields() {
        typeScope.declareField(min);
        typeScope.declareField(max);
    }

    @Override
    protected void defineFields() {
        typeScope.defineField(min, BoundNodeFactory.createCharLiteral(null, Character.MIN_VALUE));
        typeScope.defineField(max, BoundNodeFactory.createCharLiteral(null, Character.MAX_VALUE));
    }

    @Override
    protected void declareConstructors() {

    }

    @Override
    protected void defineConstructors() {

    }

    @Override
    protected void declareFunctions() {
        typeScope.declareFunction(toChar);
        typeScope.declareFunction(toString);
    }

    @Override
    protected void defineFunctions() {
        typeScope.defineFunction(toChar);
        typeScope.defineFunction(toString);
    }

    @Override
    protected void declareBinaryOperators() {
        // Binary operators for char
        typeScope.declareBinaryOperator(createBinaryOperator("+", Types.CHAR, Types.CHAR));
        typeScope.declareBinaryOperator(createBinaryOperator("-", Types.CHAR, Types.CHAR));

        typeScope.declareBinaryOperator(createBinaryOperator("==", Types.CHAR, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("!=", Types.CHAR, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">", Types.CHAR, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">=", Types.CHAR, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<", Types.CHAR, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<=", Types.CHAR, Types.BOOL));

        // Binary operators with int
        typeScope.declareBinaryOperator(createBinaryOperator("+", Types.INT, Types.CHAR));
        typeScope.declareBinaryOperator(createBinaryOperator("-", Types.INT, Types.CHAR));

        typeScope.declareBinaryOperator(createBinaryOperator("==", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("!=", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">=", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<", Types.INT, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<=", Types.INT, Types.BOOL));
    }

    @Override
    protected void defineBinaryOperators() {
        for(BinaryOperatorSymbol symbol : typeScope.getDeclaredBinaryOperators()) {
            TypeSymbol otherType = symbol.getOtherType();

            InternalBinaryOperator ibo = new InternalBinaryOperator(symbol.getName(), symbol.getOtherType(), symbol.getReturnType(), args ->{
                char self = (char) args.get(PARAM_THIS);

                if(otherType == Types.CHAR) {
                    char other = (char) args.get(PARAM_OTHER);
                    switch (symbol.getName()) {
                        case "+" -> {
                            return self + other;
                        }
                        case "-" -> {
                            return self - other;
                        }
                        case "==" -> {
                            return self == other;
                        }
                        case "!=" -> {
                            return self != other;
                        }
                        case ">" -> {
                            return self > other;
                        }
                        case ">=" -> {
                            return self >= other;
                        }
                        case "<" -> {
                            return self < other;
                        }
                        case "<=" -> {
                            return self <= other;
                        }
                    }
                } else if(otherType == Types.INT) {
                    int other = (int) args.get(PARAM_OTHER);
                    switch (symbol.getName()) {
                        case "+" -> {
                            return self + other;
                        }
                        case "-" -> {
                            return self - other;
                        }
                        case "==" -> {
                            return self == other;
                        }
                        case "!=" -> {
                            return self != other;
                        }
                        case ">" -> {
                            return self > other;
                        }
                        case ">=" -> {
                            return self >= other;
                        }
                        case "<" -> {
                            return self < other;
                        }
                        case "<=" -> {
                            return self <= other;
                        }
                    }
                }

                throw new RuntimeException("Invalid binary operator: " + symbol.getName());
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
        return Types.CHAR;
    }

    @Override
    public BoundTypeScope getTypeScope() {
        return typeScope;
    }
}
