package io.ra6.zephyr.codeanalysis.binding;

import io.ra6.zephyr.codeanalysis.binding.scopes.BoundProgramScope;
import lombok.Getter;

import java.util.List;

public class BoundProgram {
    @Getter
    private final BoundProgramScope programScope;

    public BoundProgram(BoundProgramScope programScope) {
        this.programScope = programScope;
    }

    public List<ExportSymbol> getExports() {
        return programScope.getExports();
    }
}
