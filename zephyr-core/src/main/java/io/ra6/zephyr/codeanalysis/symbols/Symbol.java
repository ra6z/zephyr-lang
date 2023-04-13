package io.ra6.zephyr.codeanalysis.symbols;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
public abstract class Symbol {
    @Getter
    private final String name;
    public abstract SymbolKind getKind();

    @Override
    public String toString() {
        return name;
    }
}
