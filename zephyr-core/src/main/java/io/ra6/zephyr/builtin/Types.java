package io.ra6.zephyr.builtin;

import io.ra6.zephyr.builtin.types.*;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Types {
    public static final TypeSymbol VOID = new TypeSymbol("void");
    public static final TypeSymbol INT = new TypeSymbol("int");
    public static final TypeSymbol STRING = new TypeSymbol("str");
    public static final TypeSymbol CHAR = new TypeSymbol("char");
    public static final TypeSymbol BOOL = new TypeSymbol("bool");
    public static final TypeSymbol ERROR = new TypeSymbol("error");
    public static final TypeSymbol DOUBLE = new TypeSymbol("double");
    public static final TypeSymbol UNKNOWN = new TypeSymbol("?");
    public static final TypeSymbol ANY = new TypeSymbol("any");

    private static final HashMap<TypeSymbol, BuiltinType> TYPES = new HashMap<>();
    private static final HashMap<Class<?>, TypeSymbol> literalTypeMapping = new HashMap<>();

    public static void register(BuiltinType type) {
        TYPES.put(type.getTypeSymbol(), type);
    }

    public static List<BuiltinType> getBuiltinTypes() {
        return new ArrayList<>(TYPES.values());
    }

    static {
        register(new BuiltinIntType());
        register(new BuiltinStringType());
        register(new BuiltinVoidType());
        register(new BuiltinDoubleType());
        register(new BuiltinBoolType());
        register(new BuiltinCharType());
        register(new BuiltinAnyType());
        // TODO: add error type
//        register(new BuiltinErrorType());

        literalTypeMapping.put(Integer.class, INT);
        literalTypeMapping.put(String.class, STRING);
        literalTypeMapping.put(Double.class, DOUBLE);
        literalTypeMapping.put(Boolean.class, BOOL);
        literalTypeMapping.put(Void.class, VOID);
        literalTypeMapping.put(Character.class, CHAR);
    }

    public static TypeSymbol getLiteralType(Class<?> literalClass) {
        return literalTypeMapping.get(literalClass);
    }

    public static boolean isBuiltinType(TypeSymbol type) {
        return TYPES.containsKey(type);
    }

    public static boolean isValidLiteralType(TypeSymbol type) {
        return literalTypeMapping.containsValue(type);
    }

    public static boolean isValidLiteralType(Class<?> aClass) {
        return literalTypeMapping.containsKey(aClass);
    }
}
