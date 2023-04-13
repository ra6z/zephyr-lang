package io.ra6.zephyr.codeanalysis.symbols;

import lombok.Getter;

public class UnaryOperatorSymbol extends Symbol {
    @Getter
    private final TypeSymbol returnType;

    public UnaryOperatorSymbol(String name, TypeSymbol returnType) {
        super(name);
        this.returnType = returnType;
    }

    @Override
    public SymbolKind getKind() {
        return SymbolKind.UNARY_OPERATOR;
    }
}
