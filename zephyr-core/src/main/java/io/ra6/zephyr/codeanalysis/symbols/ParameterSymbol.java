package io.ra6.zephyr.codeanalysis.symbols;

import lombok.Getter;

public class ParameterSymbol extends Symbol {
    @Getter
    private final TypeSymbol type;

    public ParameterSymbol(String name, TypeSymbol type) {
        super(name);
        this.type = type;
    }

    @Override
    public SymbolKind getKind() {
        return SymbolKind.PARAMETER;
    }
}
