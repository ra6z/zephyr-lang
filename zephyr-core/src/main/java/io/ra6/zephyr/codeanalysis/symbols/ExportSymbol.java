package io.ra6.zephyr.codeanalysis.symbols;

import lombok.Getter;

public class ExportSymbol extends Symbol {
    @Getter
    private final TypeSymbol type;

    public ExportSymbol(TypeSymbol type) {
        super(type.getName());
        this.type = type;
    }

    @Override
    public SymbolKind getKind() {
        return SymbolKind.EXPORT;
    }
}
