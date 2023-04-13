package io.ra6.zephyr.runtime;

import io.ra6.zephyr.codeanalysis.symbols.VariableSymbol;
import lombok.Getter;

import java.util.HashMap;

public class VariableTable extends HashMap<VariableSymbol, Object> {
    @Getter
    private final String name;

    public VariableTable(String name) {
        super();
        this.name = name;
    }

    @Override
    public String toString() {
        return "VariableTable{" +
                "name='" + name + '\'' +
                '}';
    }
}
