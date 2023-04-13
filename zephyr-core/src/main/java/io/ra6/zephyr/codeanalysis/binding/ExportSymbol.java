package io.ra6.zephyr.codeanalysis.binding;

import io.ra6.zephyr.codeanalysis.symbols.Symbol;
import io.ra6.zephyr.codeanalysis.symbols.SymbolKind;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
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
