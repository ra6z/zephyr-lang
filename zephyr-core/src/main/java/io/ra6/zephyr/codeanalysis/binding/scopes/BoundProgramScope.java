package io.ra6.zephyr.codeanalysis.binding.scopes;

import io.ra6.zephyr.codeanalysis.symbols.ExportSymbol;
import io.ra6.zephyr.codeanalysis.symbols.SymbolTable;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoundProgramScope extends BoundScope {
    @Getter
    private final String name;

    @Getter
    private final List<ExportSymbol> exports = new ArrayList<>();

    @Getter
    private final List<BoundProgramScope> importedPrograms = new ArrayList<>();

    private final SymbolTable<TypeSymbol, BoundTypeScope> types = new SymbolTable<>();
    private final HashMap<BoundProgramScope, String> debugImportedProgram = new HashMap<>();

    public BoundProgramScope(String name) {
        super(null, BoundScopeKind.PROGRAM);
        this.name = name;
    }

    public boolean isTypeDeclared(String typeName) {
        return types.getDeclarations().stream().anyMatch(t -> t.getName().equals(typeName));
    }

    public TypeSymbol getType(String typeName) {
        boolean isImported = isTypeImported(typeName);
        if (isImported) {
            return getImportedType(typeName);
        }
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

    public void importProgram(String debugName, BoundProgramScope importedProgram) {
        importedPrograms.add(importedProgram);
        debugImportedProgram.put(importedProgram, debugName);
    }

    public String getDebugImportedProgram(BoundProgramScope importedProgram) {
        return debugImportedProgram.get(importedProgram);
    }

    public boolean isTypeImported(String name) {
        return importedPrograms.stream().anyMatch(p -> p.isTypeDeclared(name));
    }

    public boolean isTypeImported(TypeSymbol type) {
        return importedPrograms.stream().anyMatch(p -> p.isTypeDeclared(type.getName()));
    }

    public TypeSymbol getImportedType(String name) {
        return importedPrograms.stream().filter(p -> p.isTypeDeclared(name)).findFirst().map(p -> p.getType(name)).orElse(null);
    }

    public BoundProgramScope getImportedProgram(String name) {
        return importedPrograms.stream().filter(p -> p.isTypeDeclared(name)).findFirst().orElse(null);
    }
}
