package io.ra6.zephyr.codeanalysis.symbols;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class VariableSymbol extends Symbol {
    @Getter
    private final boolean isReadonly;
    @Getter
    private final TypeSymbol type;

    @Setter
    @Getter
    private HashMap<String, TypeSymbol> genericTypes = new HashMap<>();

    public VariableSymbol(String name, boolean isReadonly, TypeSymbol type) {
        super(name);
        this.isReadonly = isReadonly;
        this.type = type;
    }

    public boolean isVariableGeneric() {
        return genericTypes != null && !genericTypes.isEmpty();
    }

    public TypeSymbol getGenericType(String name) {
        return genericTypes.get(name);
    }

    @Override
    public SymbolKind getKind() {
        return SymbolKind.VARIABLE;
    }

    public boolean isGenericType(String name) {
        return genericTypes.containsKey(name);
    }

    public boolean isGenericType(TypeSymbol type) {
        return isGenericType(type.getName());
    }

    public TypeSymbol getGenericType(TypeSymbol type) {
        return getGenericType(type.getName());
    }
}
