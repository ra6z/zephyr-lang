package io.ra6.zephyr.runtime;

import io.ra6.zephyr.codeanalysis.binding.scopes.BoundProgramScope;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisteredProgram {
    @Getter
    private final BoundProgramScope program;
    @Getter
    private final ProgramInterpreter evaluator;

    @Override
    public String toString() {
        return program.getName();
    }
}
