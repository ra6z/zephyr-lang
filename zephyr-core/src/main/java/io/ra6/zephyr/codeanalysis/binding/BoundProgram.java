package io.ra6.zephyr.codeanalysis.binding;

import io.ra6.zephyr.codeanalysis.binding.scopes.BoundProgramScope;
import io.ra6.zephyr.codeanalysis.symbols.ExportSymbol;
import io.ra6.zephyr.diagnostic.DiagnosticBag;
import lombok.Getter;

import java.util.List;

public class BoundProgram {
    @Getter
    private final BoundProgramScope programScope;
    @Getter
    private final DiagnosticBag diagnostics;

    public BoundProgram(BoundProgramScope programScope, DiagnosticBag diagnostics) {
        this.programScope = programScope;
        this.diagnostics = diagnostics;
    }

    public List<ExportSymbol> getExports() {
        return programScope.getExports();
    }
}
