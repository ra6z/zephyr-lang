package io.ra6.zephyr.builtin.natives;

import io.ra6.zephyr.builtin.types.BuiltinType;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import lombok.Getter;

public abstract class NativeType extends BuiltinType {
    @Getter
    private final TypeSymbol typeSymbol = new TypeSymbol(getNativeName());
    public abstract String getNativeName();
}
