package io.ra6.zephyr.codeanalysis.symbols;

import lombok.Getter;

public class BinaryOperatorSymbol extends Symbol {
    @Getter
    private final String otherOperandName;
    @Getter
    private final TypeSymbol otherType;
    @Getter
    private final TypeSymbol returnType;

    public BinaryOperatorSymbol(String operatorName, String otherOperandName, TypeSymbol otherType, TypeSymbol returnType) {
        super(operatorName);
        this.otherOperandName = otherOperandName;
        this.otherType = otherType;
        this.returnType = returnType;
    }

    @Override
    public SymbolKind getKind() {
        return SymbolKind.BINARY_OPERATOR;
    }
}
