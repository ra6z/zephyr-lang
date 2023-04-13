package io.ra6.zephyr.codeanalysis.symbols;

import io.ra6.zephyr.codeanalysis.binding.BoundNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SymbolTable<TSym extends Symbol, TDef> {
    @Getter
    private final List<TSym> declarations = new ArrayList<>();
    @Getter
    private final HashMap<TSym, TDef> definitions = new HashMap<>();

    public void declare(TSym symbol) {
        declarations.add(symbol);
    }

    public void define(TSym symbol, TDef definition) {
        definitions.put(symbol, definition);
    }

    public TDef getDefinition(TSym symbol) {
        return definitions.get(symbol);
    }
}
