package io.ra6.zephyr.codeanalysis.symbols;

import io.ra6.zephyr.codeanalysis.symbols.Symbol;
import io.ra6.zephyr.codeanalysis.symbols.SymbolKind;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import lombok.Getter;

public class VariableSymbol extends Symbol {
    @Getter
    private final boolean isReadonly;
    @Getter
    private final TypeSymbol type;

    public VariableSymbol(String name, boolean isReadonly, TypeSymbol type) {
        super(name);
        this.isReadonly = isReadonly;
        this.type = type;
    }

    @Override
    public SymbolKind getKind() {
        return SymbolKind.VARIABLE;
    }
}
