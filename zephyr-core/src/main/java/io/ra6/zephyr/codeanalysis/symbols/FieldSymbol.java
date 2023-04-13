package io.ra6.zephyr.codeanalysis.symbols;

import io.ra6.zephyr.codeanalysis.binding.Visibility;
import lombok.Getter;

public class FieldSymbol extends Symbol {
    @Getter
    private final boolean isReadonly;
    @Getter
    private final boolean isShared;
    @Getter
    private final Visibility visibility;
    @Getter
    private final TypeSymbol type;

    public FieldSymbol(String name, boolean isReadonly, boolean isShared, Visibility visibility, TypeSymbol type) {
        super(name);
        this.isReadonly = isReadonly;
        this.isShared = isShared;
        this.visibility = visibility;
        this.type = type;
    }

    @Override
    public SymbolKind getKind() {
        return SymbolKind.FIELD;
    }
}
