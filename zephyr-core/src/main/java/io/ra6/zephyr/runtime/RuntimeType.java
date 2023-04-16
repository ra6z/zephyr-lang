package io.ra6.zephyr.runtime;

import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.codeanalysis.binding.statements.BoundBlockStatement;
import io.ra6.zephyr.codeanalysis.symbols.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RuntimeType {
    private final Runtime runtime;
    @Getter
    private final TypeSymbol type;
    @Getter
    private final BoundTypeScope scope;

    @Setter
    @Getter
    private boolean initialized = false;

    private final HashMap<FieldSymbol, Object> sharedFields = new HashMap<>();

    public RuntimeType(Runtime runtime, TypeSymbol type, BoundTypeScope scope) {
        this.runtime = runtime;
        this.type = type;

        // TODO: initialize static fields
        this.scope = scope;
    }

    public List<FieldSymbol> getSharedFields() {
        return type.getFields().stream().filter(FieldSymbol::isShared).collect(Collectors.toList());
    }

    public Object assignSharedField(FieldSymbol field, Object value) {
        FieldSymbol sharedField = getSharedFields().stream().filter(f -> f.getName().equals(field.getName()) && f.getType().equals(field.getType())).findFirst().orElse(null);
        if (sharedField == null) {
            throw new RuntimeException("Field " + field.getName() + " not found in type " + type.getName() + ".");
        }
        sharedFields.put(sharedField, value);
        return value;
    }

    public Object getSharedField(FieldSymbol field) {
        return sharedFields.get(field);
    }

    public TypeInstance createInstance(HashMap<FieldSymbol, Object> fields) {
        return new TypeInstance(this, fields);
    }

    public FunctionSymbol getFunction(String functionName, boolean isShared) {
        for (FunctionSymbol function : type.getFunctions()) {
            if (function.getName().equals(functionName) && function.isShared() == isShared) {
                return function;
            }
        }

        throw new RuntimeException("Function " + functionName + " not found in type " + type.getName() + ".");
    }

    public BoundBlockStatement getFunctionBody(FunctionSymbol function) {
        BoundBlockStatement body = scope.getFunctionBody(function);
        if (body == null) {
            throw new RuntimeException("Function " + function.getName() + " not found in type " + type.getName() + ".");
        }
        return body;
    }

    public String getName() {
        return type.getName();
    }

    public BinaryOperatorSymbol getBinaryOperator(String operator, RuntimeType rightType) {
        return type.getBinaryOperator(operator, rightType.getType());
    }

    public UnaryOperatorSymbol getUnaryOperator(String operator) {
        return type.getUnaryOperator(operator);
    }
}
