package io.ra6.zephyr.runtime;

import io.ra6.zephyr.codeanalysis.symbols.FieldSymbol;
import lombok.Getter;

import java.util.HashMap;

public class TypeInstance {
    @Getter
    private final RuntimeType runtimeType;
    private final HashMap<FieldSymbol, Object> fields;

    @Getter
    private final HashMap<String, RuntimeType> genericTypes = new HashMap<>();

    public TypeInstance(RuntimeType runtimeType, HashMap<FieldSymbol, Object> fields, HashMap<String, RuntimeType> genericTypes) {
        this.runtimeType = runtimeType;
        this.fields = fields;
        this.genericTypes.putAll(genericTypes);
    }

    public FieldSymbol lookupField(String name) {
        for (FieldSymbol field : runtimeType.getType().getFields()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }

        throw new RuntimeException("Field " + name + " not found in type " + runtimeType.getType().getName() + ".");
    }

    public Object getField(FieldSymbol field) {
        return fields.get(field);
    }

    public void setField(FieldSymbol field, Object value) {
        fields.put(field, value);
    }

    public RuntimeType getGenericType(String name) {
        return genericTypes.get(name);
    }

    public boolean hasGenerics() {
        return !genericTypes.isEmpty();
    }
}
