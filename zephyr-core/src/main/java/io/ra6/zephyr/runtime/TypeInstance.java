package io.ra6.zephyr.runtime;

import io.ra6.zephyr.codeanalysis.symbols.FieldSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.symbols.VariableSymbol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

@RequiredArgsConstructor
public class TypeInstance {
    @Getter
    private final TypeSymbol type;
    @Getter
    private final HashMap<VariableSymbol, Object> fields;

    private VariableSymbol getVariableSymbolByName(String name) {
        for (VariableSymbol variableSymbol : fields.keySet()) {
            if (variableSymbol.getName().equals(name)) {
                return variableSymbol;
            }
        }
        return null;
    }

    private Object getFieldByName(String name) {
        for (VariableSymbol variableSymbol : fields.keySet()) {
            if (variableSymbol.getName().equals(name)) {
                return fields.get(variableSymbol);
            }
        }
        return null;
    }

    public Object getFieldValue(VariableSymbol field) {
        return getFieldByName(field.getName());
    }

    public Object getFieldValue(FieldSymbol field) {
        return getFieldByName(field.getName());
    }

    public void setField(VariableSymbol field, Object value) {
        setField(type.getField(field.getName()), value);
    }

    public void setField(FieldSymbol field, Object value) {
        fields.put(getVariableSymbolByName(field.getName()), value);
    }
}
