package io.ra6.zephyr.codeanalysis.symbols;

import lombok.Getter;

public class ArrayTypeSymbol extends TypeSymbol {
    @Getter
    private final TypeSymbol elementType;

    public ArrayTypeSymbol(TypeSymbol elementType) {
        super(elementType.getName() + "[]");
        this.elementType = elementType;
    }
}
