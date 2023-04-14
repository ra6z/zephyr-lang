package io.ra6.zephyr.builtin.types;

import io.ra6.zephyr.codeanalysis.binding.Visibility;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.builtin.BuiltinTypes;
import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.codeanalysis.symbols.ParameterSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;

import java.util.List;

public class BuiltinStringType extends BuiltinType {

    private final BoundTypeScope typeScope = new BoundTypeScope(null, BuiltinTypes.STRING);

    private final InternalFunction length = new InternalFunction("length", false, Visibility.PUBLIC, List.of(), BuiltinTypes.INT, args -> {
        String thisValue = (String) args.get("this");
        return thisValue.length();
    });

    private final InternalFunction charAt = new InternalFunction("charAt", false, Visibility.PUBLIC, List.of(new ParameterSymbol("index", BuiltinTypes.INT)), BuiltinTypes.INT, args -> {
        String stringValue = (String) args.get("this");
        int index = (int) args.get("index");
        return (int) stringValue.charAt(index);
    });

    private final InternalFunction substringSE = new InternalFunction("substringSE", false, Visibility.PUBLIC, List.of(new ParameterSymbol("start", BuiltinTypes.INT), new ParameterSymbol("end", BuiltinTypes.INT)), BuiltinTypes.STRING, args -> {
        String otherValue = (String) args.get("this");
        int start = (int) args.get("start");
        int end = (int) args.get("end");
        return otherValue.substring(start, end);
    });

    private final InternalFunction substringS = new InternalFunction("substringS", false, Visibility.PUBLIC, List.of(new ParameterSymbol("start", BuiltinTypes.INT)), BuiltinTypes.STRING, args -> {
        String otherValue = (String) args.get("this");
        int start = (int) args.get("start");
        return otherValue.substring(start);
    });

    private final InternalFunction repeat = new InternalFunction("repeat", true, Visibility.PUBLIC, List.of(new ParameterSymbol("string", BuiltinTypes.STRING), new ParameterSymbol("count", BuiltinTypes.INT)), BuiltinTypes.STRING, args -> {
        String otherValue = (String) args.get("string");
        int count = (int) args.get("count");
        return otherValue.repeat(count);
    });

    private final InternalFunction replace = new InternalFunction("replace", false, Visibility.PUBLIC, List.of(new ParameterSymbol("old", BuiltinTypes.STRING), new ParameterSymbol("new", BuiltinTypes.STRING)), BuiltinTypes.STRING, args -> {
        String otherValue = (String) args.get("this");
        String old = (String) args.get("old");
        String newString = (String) args.get("new");
        return otherValue.replace(old, newString);
    });


    private final InternalFunction contains = new InternalFunction("contains", false, Visibility.PUBLIC, List.of(new ParameterSymbol("other", BuiltinTypes.STRING)), BuiltinTypes.BOOL, args -> {
        String otherValue = (String) args.get("this");
        String other = (String) args.get("other");
        return otherValue.contains(other);
    });

    private final InternalFunction equals = new InternalFunction("equals", false, Visibility.PUBLIC, List.of(new ParameterSymbol("other", BuiltinTypes.STRING)), BuiltinTypes.BOOL, args -> {
        String otherValue = (String) args.get("this");
        String other = (String) args.get("other");
        return otherValue.equals(other);
    });

    private final InternalFunction equalsIgnoreCase = new InternalFunction("equalsIgnoreCase", false, Visibility.PUBLIC, List.of(new ParameterSymbol("other", BuiltinTypes.STRING)), BuiltinTypes.BOOL, args -> {
        String otherValue = (String) args.get("this");
        String other = (String) args.get("other");
        return otherValue.equalsIgnoreCase(other);
    });

    private final InternalFunction startsWith = new InternalFunction("startsWith", false, Visibility.PUBLIC, List.of(new ParameterSymbol("other", BuiltinTypes.STRING)), BuiltinTypes.BOOL, args -> {
        String otherValue = (String) args.get("this");
        String other = (String) args.get("other");
        return otherValue.startsWith(other);
    });

    private final InternalFunction endsWith = new InternalFunction("endsWith", false, Visibility.PUBLIC, List.of(new ParameterSymbol("other", BuiltinTypes.STRING)), BuiltinTypes.BOOL, args -> {
        String otherValue = (String) args.get("this");
        String other = (String) args.get("other");
        return otherValue.endsWith(other);
    });

    private final InternalFunction isEmpty = new InternalFunction("isEmpty", true, Visibility.PUBLIC, List.of(), BuiltinTypes.BOOL, args -> {
        String str = (String) args.get("this");
        return str.isEmpty();
    });

    private final InternalFunction toLowerCase = new InternalFunction("toLowerCase", true, Visibility.PUBLIC, List.of(), BuiltinTypes.STRING, args -> {
        String str = (String) args.get("this");
        return str.toLowerCase();
    });

    private final InternalFunction toUpperCase = new InternalFunction("toUpperCase", true, Visibility.PUBLIC, List.of(), BuiltinTypes.STRING, args -> {
        String str = (String) args.get("this");
        return str.toUpperCase();
    });

    private final InternalFunction trim = new InternalFunction("trim", true, Visibility.PUBLIC, List.of(), BuiltinTypes.STRING, args -> {
        String str = (String) args.get("this");
        return str.trim();
    });

    private final InternalFunction trimLeft = new InternalFunction("trimLeft", true, Visibility.PUBLIC, List.of(), BuiltinTypes.STRING, args -> {
        String str = (String) args.get("this");
        int i = 0;
        while (i < str.length() && Character.isWhitespace(str.charAt(i))) {
            i++;
        }
        return str.substring(i);
    });

    private final InternalFunction trimRight = new InternalFunction("trimRight", true, Visibility.PUBLIC, List.of(), BuiltinTypes.STRING, args -> {
        String str = (String) args.get("this");
        int i = str.length() - 1;
        while (i >= 0 && Character.isWhitespace(str.charAt(i))) {
            i--;
        }
        return str.substring(0, i + 1);
    });

    private final InternalFunction valueOfInt = new InternalFunction("valueOfInt", true, Visibility.PUBLIC, List.of(new ParameterSymbol("value", BuiltinTypes.INT)), BuiltinTypes.STRING, args -> {
        int i = (int) args.get("value");
        return String.valueOf(i);
    });

    private final InternalFunction valueOfDouble = new InternalFunction("valueOfDouble", true, Visibility.PUBLIC, List.of(new ParameterSymbol("value", BuiltinTypes.DOUBLE)), BuiltinTypes.STRING, args -> {
        double d = (double) args.get("value");
        return String.valueOf(d);
    });

    private final InternalFunction valueOfBool = new InternalFunction("valueOfBool", true, Visibility.PUBLIC, List.of(new ParameterSymbol("value", BuiltinTypes.BOOL)), BuiltinTypes.STRING, args -> {
        boolean b = (boolean) args.get("value");
        return String.valueOf(b);
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
        typeScope.declareFunction(length.getFunctionSymbol());
        typeScope.declareFunction(charAt.getFunctionSymbol());
        typeScope.declareFunction(substringSE.getFunctionSymbol());
        typeScope.declareFunction(substringS.getFunctionSymbol());
        typeScope.declareFunction(repeat.getFunctionSymbol());
        typeScope.declareFunction(contains.getFunctionSymbol());
        typeScope.declareFunction(equals.getFunctionSymbol());
        typeScope.declareFunction(equalsIgnoreCase.getFunctionSymbol());
        typeScope.declareFunction(startsWith.getFunctionSymbol());
        typeScope.declareFunction(endsWith.getFunctionSymbol());
        typeScope.declareFunction(isEmpty.getFunctionSymbol());
        typeScope.declareFunction(toLowerCase.getFunctionSymbol());
        typeScope.declareFunction(toUpperCase.getFunctionSymbol());
        typeScope.declareFunction(trim.getFunctionSymbol());
        typeScope.declareFunction(trimLeft.getFunctionSymbol());
        typeScope.declareFunction(trimRight.getFunctionSymbol());
        typeScope.declareFunction(replace.getFunctionSymbol());
        typeScope.declareFunction(valueOfInt.getFunctionSymbol());
        typeScope.declareFunction(valueOfDouble.getFunctionSymbol());
        typeScope.declareFunction(valueOfBool.getFunctionSymbol());
    }

    @Override
    protected void defineFunctions() {
        typeScope.defineFunction(length.getFunctionSymbol(), length.bindBody());
        typeScope.defineFunction(charAt.getFunctionSymbol(), charAt.bindBody());
        typeScope.defineFunction(substringSE.getFunctionSymbol(), substringSE.bindBody());
        typeScope.defineFunction(substringS.getFunctionSymbol(), substringS.bindBody());
        typeScope.defineFunction(repeat.getFunctionSymbol(), repeat.bindBody());
        typeScope.defineFunction(contains.getFunctionSymbol(), contains.bindBody());
        typeScope.defineFunction(equals.getFunctionSymbol(), equals.bindBody());
        typeScope.defineFunction(equalsIgnoreCase.getFunctionSymbol(), equalsIgnoreCase.bindBody());
        typeScope.defineFunction(startsWith.getFunctionSymbol(), startsWith.bindBody());
        typeScope.defineFunction(endsWith.getFunctionSymbol(), endsWith.bindBody());
        typeScope.defineFunction(isEmpty.getFunctionSymbol(), isEmpty.bindBody());
        typeScope.defineFunction(toLowerCase.getFunctionSymbol(), toLowerCase.bindBody());
        typeScope.defineFunction(toUpperCase.getFunctionSymbol(), toUpperCase.bindBody());
        typeScope.defineFunction(trim.getFunctionSymbol(), trim.bindBody());
        typeScope.defineFunction(trimLeft.getFunctionSymbol(), trimLeft.bindBody());
        typeScope.defineFunction(trimRight.getFunctionSymbol(), trimRight.bindBody());
        typeScope.defineFunction(replace.getFunctionSymbol(), replace.bindBody());
        typeScope.defineFunction(valueOfInt.getFunctionSymbol(), valueOfInt.bindBody());
        typeScope.defineFunction(valueOfDouble.getFunctionSymbol(), valueOfDouble.bindBody());
        typeScope.defineFunction(valueOfBool.getFunctionSymbol(), valueOfBool.bindBody());
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
        return BuiltinTypes.STRING;
    }

    @Override
    public BoundTypeScope getTypeScope() {
        return typeScope;
    }

}
