package io.ra6.zephyr.codeanalysis.symbols;

import lombok.Getter;

import java.util.List;

public class ConstructorSymbol extends CallableSymbol {
    @Getter
    private final List<ParameterSymbol> parameters;

    public ConstructorSymbol(List<ParameterSymbol> parameters) {
        super("constructor");
        this.parameters = parameters;
    }

    @Override
    public SymbolKind getKind() {
        return SymbolKind.CONSTRUCTOR;
    }
}
