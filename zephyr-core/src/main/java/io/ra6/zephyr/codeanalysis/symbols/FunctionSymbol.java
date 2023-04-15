package io.ra6.zephyr.codeanalysis.symbols;

import io.ra6.zephyr.codeanalysis.binding.Visibility;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FunctionSymbol extends CallableSymbol{
    @Getter
    private final boolean isShared;
    @Getter
    @Setter
    private Visibility visibility;
    @Getter
    private final List<ParameterSymbol> parameters;
    @Getter
    private final TypeSymbol type;

    public FunctionSymbol(String name, boolean isShared, Visibility visibility, List<ParameterSymbol> parameters, TypeSymbol type) {
        super(name);
        this.isShared = isShared;
        this.visibility = visibility;
        this.parameters = parameters;
        this.type = type;
    }

    @Override
    public SymbolKind getKind() {
        return SymbolKind.FUNCTION;
    }
}
