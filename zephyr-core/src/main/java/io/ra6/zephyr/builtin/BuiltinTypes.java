package io.ra6.zephyr.builtin;

import io.ra6.zephyr.builtin.types.*;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuiltinTypes {
    public static final TypeSymbol VOID = new TypeSymbol("void");
    public static final TypeSymbol INT = new TypeSymbol("int");
    public static final TypeSymbol STRING = new TypeSymbol("str");
    public static final TypeSymbol BOOL = new TypeSymbol("bool");
    public static final TypeSymbol ERROR = new TypeSymbol("error");
    public static final TypeSymbol DOUBLE = new TypeSymbol("double");

    private static final HashMap<TypeSymbol, BuiltinType> types = new HashMap<>();

    public static void register(BuiltinType type) {
        types.put(type.getTypeSymbol(), type);
    }

    public static List<BuiltinType> getBuiltinTypes() {
        return new ArrayList<>(types.values());
    }

    static {
        register(new BuiltinIntType());
        register(new BuiltinStringType());
        register(new BuiltinVoidType());
        register(new BuiltinDoubleType());
    }

    public static boolean isBuiltinType(TypeSymbol type) {
        return types.containsKey(type);
    }
}
