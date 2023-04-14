package io.ra6.zephyr.codeanalysis.binding.scopes;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.statements.BoundBlockStatement;
import io.ra6.zephyr.codeanalysis.symbols.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoundTypeScope extends BoundScope {
    @Getter
    private final TypeSymbol type;

    @Getter
    private final List<Symbol> declaredFieldsAndFunctions = new ArrayList<>();
    private final HashMap<FieldSymbol, BoundExpression> fieldDefinitions = new HashMap<>();
    private final HashMap<FunctionSymbol, BoundBlockStatement> functionDefinitions = new HashMap<>();

    private final SymbolTable<ConstructorSymbol, BoundBlockStatement> constructors = new SymbolTable<>();
    private final SymbolTable<BinaryOperatorSymbol, BoundBlockStatement> binaryOperators = new SymbolTable<>();
    private final SymbolTable<UnaryOperatorSymbol, BoundBlockStatement> unaryOperators = new SymbolTable<>();

    @Getter
    private final List<String> declaredGenericTypes = new ArrayList<>();

    public BoundTypeScope(BoundScope parent, TypeSymbol type) {
        super(parent, BoundScopeKind.TYPE);
        this.type = type;
    }

    public List<ConstructorSymbol> getDeclaredConstructors() {
        return constructors.getDeclarations();
    }

    public List<BinaryOperatorSymbol> getDeclaredBinaryOperators() {
        return binaryOperators.getDeclarations();
    }

    public List<UnaryOperatorSymbol> getDeclaredUnaryOperators() {
        return unaryOperators.getDeclarations();
    }


    // TODO: support overloading


    public boolean isConstructorDefined(int args) {
        return getDeclaredConstructors().stream().anyMatch(c -> c.getParameters().size() == args);
    }

    public void declareConstructor(ConstructorSymbol constructor) {
        constructors.declare(constructor);
    }

    public void defineConstructor(ConstructorSymbol constructor, BoundBlockStatement body) {
        if (!isConstructorDefined(constructor.getParameters().size())) {
            throw new RuntimeException("Constructor with " + constructor.getParameters().size() + " arguments is not declared.");
        }

        constructors.define(constructor, body);
    }

    public boolean isBinaryOperatorDeclared(String operatorName, TypeSymbol other) {
        return getDeclaredBinaryOperators().stream().anyMatch(f -> f.getName().equals(operatorName) && f.getOtherType() == other);
    }

    public void declareBinaryOperator(BinaryOperatorSymbol operator) {
        binaryOperators.declare(operator);
    }

    public void defineBinaryOperator(BinaryOperatorSymbol operator, BoundBlockStatement body) {
        if (!isBinaryOperatorDeclared(operator.getName(), operator.getOtherType())) {
            throw new RuntimeException("Binary operator " + operator.getName() + " is not declared.");
        }

        binaryOperators.define(operator, body);
    }

    public boolean isUnaryOperatorDeclared(String operatorName) {
        return getDeclaredUnaryOperators().stream().anyMatch(f -> f.getName().equals(operatorName));
    }

    public void declareUnaryOperator(UnaryOperatorSymbol operator) {
        unaryOperators.declare(operator);
    }

    public void defineUnaryOperator(UnaryOperatorSymbol operator, BoundBlockStatement body) {
        if (!isUnaryOperatorDeclared(operator.getName())) {
            throw new RuntimeException("Unary operator " + operator.getName() + " is not declared.");
        }

        unaryOperators.define(operator, body);
    }

    public BinaryOperatorSymbol getBinaryOperator(String operatorName, TypeSymbol other) {
        return binaryOperators.getDeclarations().stream().filter(f -> f.getName().equals(operatorName) && f.getOtherType() == other).findFirst().orElse(null);
    }

    public UnaryOperatorSymbol getUnaryOperator(String operatorName) {
        return unaryOperators.getDeclarations().stream().filter(f -> f.getName().equals(operatorName)).findFirst().orElse(null);
    }

    public boolean isFieldOrFunctionDeclared(String name) {
        return declaredFieldsAndFunctions.stream().anyMatch(f -> f.getName().equals(name));
    }

    public boolean isField(String name) {
        return declaredFieldsAndFunctions.stream().anyMatch(f -> f.getName().equals(name) && f.getKind() == SymbolKind.FIELD);
    }

    public boolean isFunction(String name) {
        return declaredFieldsAndFunctions.stream().anyMatch(f -> f.getName().equals(name) && f.getKind() == SymbolKind.FUNCTION);
    }

    public void declareField(FieldSymbol field) {
        declaredFieldsAndFunctions.add(field);
    }

    public void declareFunction(FunctionSymbol function) {
        declaredFieldsAndFunctions.add(function);
    }

    public FieldSymbol getField(String fieldName) {
        return declaredFieldsAndFunctions.stream()
                .filter(f -> f.getName().equals(fieldName) && f.getKind() == SymbolKind.FIELD)
                .map(f -> (FieldSymbol) f)
                .findFirst()
                .orElse(null);
    }

    public FunctionSymbol getFunction(String functionName) {
        return declaredFieldsAndFunctions.stream()
                .filter(f -> f.getName().equals(functionName) && f.getKind() == SymbolKind.FUNCTION)
                .map(f -> (FunctionSymbol) f)
                .findFirst()
                .orElse(null);
    }

    public void defineField(FieldSymbol field, BoundExpression initializer) {
        fieldDefinitions.put(field, initializer);
    }

    public void defineFunction(FunctionSymbol function, BoundBlockStatement body) {
        functionDefinitions.put(function, body);
    }

    public BoundBlockStatement getFunctionScope(FunctionSymbol function) {
        return functionDefinitions
                .keySet()
                .stream()
                .filter(f -> f.getName().equals(function.getName()) && f.getParameters().size() == function.getParameters().size() && f.getType() == function.getType() && f.getVisibility() == function.getVisibility() && f.isShared() == function.isShared())
                .findFirst()
                .map(functionDefinitions::get)
                .orElse(null);
    }

    public BoundBlockStatement getConstructorScope(ConstructorSymbol constructor) {
        return constructors.getDefinitions()
                .keySet()
                .stream()
                .filter(f -> f.getParameters().size() == constructor.getParameters().size())
                .findFirst()
                .map(constructors::getDefinition)
                .orElse(null);
    }

    public BoundExpression getFieldInitializer(FieldSymbol field) {
        return fieldDefinitions
                .keySet()
                .stream()
                .filter(f -> f.getName().equals(field.getName()) && f.getType() == field.getType() && f.getVisibility() == field.getVisibility() && f.isShared() == field.isShared())
                .findFirst()
                .map(fieldDefinitions::get)
                .orElse(null);
    }

    public BoundBlockStatement getUnaryOperatorScope(UnaryOperatorSymbol operator) {
        return unaryOperators.getDefinitions()
                .keySet()
                .stream()
                .filter(f -> f.getName().equals(operator.getName()))
                .findFirst()
                .map(unaryOperators::getDefinition)
                .orElse(null);
    }

    public BoundBlockStatement getBinaryOperatorScope(BinaryOperatorSymbol binaryOperator) {
        return binaryOperators.getDefinitions()
                .keySet()
                .stream()
                .filter(f -> f.getName().equals(binaryOperator.getName()) && f.getOtherType() == binaryOperator.getOtherType())
                .findFirst()
                .map(binaryOperators::getDefinition)
                .orElse(null);
    }

    public boolean isGenericDeclared(String genericName) {
        return declaredGenericTypes.stream().anyMatch(g -> g.equals(genericName));
    }

    public void declareGeneric(String genericName) {
        declaredGenericTypes.add(genericName);
    }
}
