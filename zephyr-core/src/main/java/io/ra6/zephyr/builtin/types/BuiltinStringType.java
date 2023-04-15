package io.ra6.zephyr.builtin.types;

import io.ra6.zephyr.builtin.InternalBinaryOperator;
import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.builtin.InternalUnaryOperator;
import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.codeanalysis.symbols.BinaryOperatorSymbol;
import io.ra6.zephyr.codeanalysis.symbols.ParameterSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.symbols.UnaryOperatorSymbol;

import java.util.List;

import static io.ra6.zephyr.builtin.IFunctionBase.PARAM_OTHER;
import static io.ra6.zephyr.builtin.IFunctionBase.PARAM_THIS;

public class BuiltinStringType extends BuiltinType {

    private final BoundTypeScope typeScope = new BoundTypeScope(null, Types.STRING);

    private final InternalFunction length = new InternalFunction("length", false, Visibility.PUBLIC, List.of(), Types.INT, args -> {
        String thisValue = (String) args.get(PARAM_THIS);
        return thisValue.length();
    });

    private final InternalFunction charAt = new InternalFunction("charAt", false, Visibility.PUBLIC, List.of(new ParameterSymbol("index", Types.INT)), Types.INT, args -> {
        String stringValue = (String) args.get(PARAM_THIS);
        int index = (int) args.get("index");
        return (int) stringValue.charAt(index);
    });

    private final InternalFunction substringSE = new InternalFunction("substringSE", false, Visibility.PUBLIC, List.of(new ParameterSymbol("start", Types.INT), new ParameterSymbol("end", Types.INT)), Types.STRING, args -> {
        String otherValue = (String) args.get(PARAM_THIS);
        int start = (int) args.get("start");
        int end = (int) args.get("end");
        return otherValue.substring(start, end);
    });

    private final InternalFunction substringS = new InternalFunction("substringS", false, Visibility.PUBLIC, List.of(new ParameterSymbol("start", Types.INT)), Types.STRING, args -> {
        String otherValue = (String) args.get(PARAM_THIS);
        int start = (int) args.get("start");
        return otherValue.substring(start);
    });

    private final InternalFunction repeat = new InternalFunction("repeat", true, Visibility.PUBLIC, List.of(new ParameterSymbol("string", Types.STRING), new ParameterSymbol("count", Types.INT)), Types.STRING, args -> {
        String otherValue = (String) args.get("string");
        int count = (int) args.get("count");
        return otherValue.repeat(count);
    });

    private final InternalFunction replace = new InternalFunction("replace", false, Visibility.PUBLIC, List.of(new ParameterSymbol("old", Types.STRING), new ParameterSymbol("new", Types.STRING)), Types.STRING, args -> {
        String otherValue = (String) args.get(PARAM_THIS);
        String old = (String) args.get("old");
        String newString = (String) args.get("new");
        return otherValue.replace(old, newString);
    });


    private final InternalFunction contains = new InternalFunction("contains", false, Visibility.PUBLIC, List.of(new ParameterSymbol(PARAM_OTHER, Types.STRING)), Types.BOOL, args -> {
        String otherValue = (String) args.get(PARAM_THIS);
        String other = (String) args.get(PARAM_OTHER);
        return otherValue.contains(other);
    });

    private final InternalFunction equals = new InternalFunction("equals", false, Visibility.PUBLIC, List.of(new ParameterSymbol(PARAM_OTHER, Types.STRING)), Types.BOOL, args -> {
        String otherValue = (String) args.get(PARAM_THIS);
        String other = (String) args.get(PARAM_OTHER);
        return otherValue.equals(other);
    });

    private final InternalFunction equalsIgnoreCase = new InternalFunction("equalsIgnoreCase", false, Visibility.PUBLIC, List.of(new ParameterSymbol(PARAM_OTHER, Types.STRING)), Types.BOOL, args -> {
        String otherValue = (String) args.get(PARAM_THIS);
        String other = (String) args.get(PARAM_OTHER);
        return otherValue.equalsIgnoreCase(other);
    });

    private final InternalFunction startsWith = new InternalFunction("startsWith", false, Visibility.PUBLIC, List.of(new ParameterSymbol(PARAM_OTHER, Types.STRING)), Types.BOOL, args -> {
        String otherValue = (String) args.get(PARAM_THIS);
        String other = (String) args.get(PARAM_OTHER);
        return otherValue.startsWith(other);
    });

    private final InternalFunction endsWith = new InternalFunction("endsWith", false, Visibility.PUBLIC, List.of(new ParameterSymbol(PARAM_OTHER, Types.STRING)), Types.BOOL, args -> {
        String otherValue = (String) args.get(PARAM_THIS);
        String other = (String) args.get(PARAM_OTHER);
        return otherValue.endsWith(other);
    });

    private final InternalFunction isEmpty = new InternalFunction("isEmpty", true, Visibility.PUBLIC, List.of(), Types.BOOL, args -> {
        String str = (String) args.get(PARAM_THIS);
        return str.isEmpty();
    });

    private final InternalFunction toLowerCase = new InternalFunction("toLowerCase", true, Visibility.PUBLIC, List.of(), Types.STRING, args -> {
        String str = (String) args.get(PARAM_THIS);
        return str.toLowerCase();
    });

    private final InternalFunction toUpperCase = new InternalFunction("toUpperCase", true, Visibility.PUBLIC, List.of(), Types.STRING, args -> {
        String str = (String) args.get(PARAM_THIS);
        return str.toUpperCase();
    });

    private final InternalFunction trim = new InternalFunction("trim", true, Visibility.PUBLIC, List.of(), Types.STRING, args -> {
        String str = (String) args.get(PARAM_THIS);
        return str.trim();
    });

    private final InternalFunction trimLeft = new InternalFunction("trimLeft", true, Visibility.PUBLIC, List.of(), Types.STRING, args -> {
        String str = (String) args.get(PARAM_THIS);
        int i = 0;
        while (i < str.length() && Character.isWhitespace(str.charAt(i))) {
            i++;
        }
        return str.substring(i);
    });

    private final InternalFunction trimRight = new InternalFunction("trimRight", true, Visibility.PUBLIC, List.of(), Types.STRING, args -> {
        String str = (String) args.get(PARAM_THIS);
        int i = str.length() - 1;
        while (i >= 0 && Character.isWhitespace(str.charAt(i))) {
            i--;
        }
        return str.substring(0, i + 1);
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
        typeScope.declareFunction(length);
        typeScope.declareFunction(charAt);
        typeScope.declareFunction(substringSE);
        typeScope.declareFunction(substringS);
        typeScope.declareFunction(repeat);
        typeScope.declareFunction(contains);
        typeScope.declareFunction(equals);
        typeScope.declareFunction(equalsIgnoreCase);
        typeScope.declareFunction(startsWith);
        typeScope.declareFunction(endsWith);
        typeScope.declareFunction(isEmpty);
        typeScope.declareFunction(toLowerCase);
        typeScope.declareFunction(toUpperCase);
        typeScope.declareFunction(trim);
        typeScope.declareFunction(trimLeft);
        typeScope.declareFunction(trimRight);
        typeScope.declareFunction(replace);
    }

    @Override
    protected void defineFunctions() {
        typeScope.defineFunction(length);
        typeScope.defineFunction(charAt);
        typeScope.defineFunction(substringSE);
        typeScope.defineFunction(substringS);
        typeScope.defineFunction(repeat);
        typeScope.defineFunction(contains);
        typeScope.defineFunction(equals);
        typeScope.defineFunction(equalsIgnoreCase);
        typeScope.defineFunction(startsWith);
        typeScope.defineFunction(endsWith);
        typeScope.defineFunction(isEmpty);
        typeScope.defineFunction(toLowerCase);
        typeScope.defineFunction(toUpperCase);
        typeScope.defineFunction(trim);
        typeScope.defineFunction(trimLeft);
        typeScope.defineFunction(trimRight);
        typeScope.defineFunction(replace);
    }

    @Override
    protected void declareBinaryOperators() {
        // concat strings
        typeScope.declareBinaryOperator(createBinaryOperator("+", Types.STRING, Types.STRING));

        // compare strings
        typeScope.declareBinaryOperator(createBinaryOperator("==", Types.STRING, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("!=", Types.STRING, Types.BOOL));
        // compare string lengths
        typeScope.declareBinaryOperator(createBinaryOperator("<", Types.STRING, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator("<=", Types.STRING, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">", Types.STRING, Types.BOOL));
        typeScope.declareBinaryOperator(createBinaryOperator(">=", Types.STRING, Types.BOOL));


    }

    @Override
    protected void defineBinaryOperators() {
        for (BinaryOperatorSymbol symbol : typeScope.getDeclaredBinaryOperators()) {
            TypeSymbol otherType = symbol.getOtherType();

            InternalBinaryOperator ibo = new InternalBinaryOperator(symbol.getName(), otherType, symbol.getReturnType(), (args) -> {
                String thisValue = (String) args.get(PARAM_THIS);
                String otherValue = (String) args.get(PARAM_OTHER);

                return switch (symbol.getName()) {
                    case "+" -> thisValue + otherValue;
                    case "==" -> thisValue.equals(otherValue);
                    case "!=" -> !thisValue.equals(otherValue);
                    case "<" -> thisValue.length() < otherValue.length();
                    case "<=" -> thisValue.length() <= otherValue.length();
                    case ">" -> thisValue.length() > otherValue.length();
                    case ">=" -> thisValue.length() >= otherValue.length();
                    default -> throw new RuntimeException("Unknown binary operator: " + symbol.getName());
                };
            });

            typeScope.defineBinaryOperator(symbol, ibo.bindBody());
        }
    }

    @Override
    protected void declareUnaryOperators() {
        // lowercase characters to uppercase characters and vice versa
        typeScope.declareUnaryOperator(createUnaryOperator("~", Types.STRING));
    }

    @Override
    protected void defineUnaryOperators() {
        for (UnaryOperatorSymbol symbol : typeScope.getDeclaredUnaryOperators()) {
            InternalUnaryOperator iuo = new InternalUnaryOperator(symbol.getName(), symbol.getReturnType(), (args) -> {
                String thisValue = (String) args.get(PARAM_THIS);

                if (symbol.getName().equals("~")) {
                    StringBuilder result = new StringBuilder();
                    for (int i = 0; i < thisValue.length(); i++) {
                        char c = thisValue.charAt(i);
                        if (Character.isUpperCase(c)) {
                            result.append(Character.toLowerCase(c));
                        } else if (Character.isLowerCase(c)) {
                            result.append(Character.toUpperCase(c));
                        } else {
                            result.append(c);
                        }
                    }
                    return result.toString();
                }

                throw new RuntimeException("Unknown unary operator: " + symbol.getName());
            });

            typeScope.defineUnaryOperator(symbol, iuo.bindBody());
        }
    }

    @Override
    public TypeSymbol getTypeSymbol() {
        return Types.STRING;
    }

    @Override
    public BoundTypeScope getTypeScope() {
        return typeScope;
    }

}
