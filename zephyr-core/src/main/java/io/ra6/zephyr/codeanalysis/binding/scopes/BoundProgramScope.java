package io.ra6.zephyr.codeanalysis.binding.scopes;

import io.ra6.zephyr.codeanalysis.binding.ExportSymbol;
import io.ra6.zephyr.codeanalysis.symbols.SymbolTable;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BoundProgramScope extends BoundScope {

    @Getter
    private final List<ExportSymbol> exports = new ArrayList<>();
    private final List<TypeSymbol> importedTypes = new ArrayList<>();

    private final SymbolTable<TypeSymbol, BoundTypeScope> types = new SymbolTable<>();

    public BoundProgramScope() {
        super(null, BoundScopeKind.PROGRAM);
    }

    public boolean isTypeDeclared(String typeName) {
        return types.getDeclarations().stream().anyMatch(t -> t.getName().equals(typeName));
    }

    public TypeSymbol getType(String typeName) {
        return types.getDeclarations().stream().filter(t -> t.getName().equals(typeName)).findFirst().orElse(null);
    }

    public BoundTypeScope getTypeScope(TypeSymbol symbol) {
        return types.getDefinition(symbol);
    }

    public void declareType(TypeSymbol type) {
        types.declare(type);
    }

    public void defineType(TypeSymbol type, BoundTypeScope typeScope) {
        if (!isTypeDeclared(type.getName())) {
            throw new RuntimeException("Type " + type.getName() + " is not declared.");
        }

        types.define(type, typeScope);
    }

    public boolean isExportDeclared(String exportName) {
        return exports.stream().anyMatch(e -> e.getName().equals(exportName));
    }

    public void declareExport(ExportSymbol export) {
        exports.add(export);
    }

    public void importType(TypeSymbol type, BoundTypeScope typeScope) {
        importedTypes.add(type);
        types.declare(type);
        types.define(type, typeScope);
    }

    public boolean isTypeImported(String name) {
        return importedTypes.stream().anyMatch(t -> t.getName().equals(name));
    }
}
