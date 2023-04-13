package io.ra6.zephyr.codeanalysis.binding.scopes;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.symbols.SymbolTable;
import io.ra6.zephyr.codeanalysis.symbols.VariableSymbol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BoundScope {
    @Getter
    private final BoundScope parent;
    @Getter
    private final BoundScopeKind kind;

    private final SymbolTable<VariableSymbol, BoundExpression> variables = new SymbolTable<>();

    public List<VariableSymbol> getDeclaredVariables() {
        return variables.getDeclarations();
    }

    public boolean isVariableDeclared(String variableName) {
        return getDeclaredVariables().stream().anyMatch(f -> f.getName().equals(variableName));
    }

    public void defineVariable(VariableSymbol variable, BoundExpression initializer) {
        variables.declare(variable);
        variables.define(variable, initializer);
    }

    public VariableSymbol getVariable(String variableName) {
        for (var scope = this; scope != null; scope = scope.getParent()) {
            var variable = scope.variables.getDeclarations().stream().filter(f -> f.getName().equals(variableName)).findFirst().orElse(null);
            if (variable != null) {
                return variable;
            }
        }
        return null;
    }
}
