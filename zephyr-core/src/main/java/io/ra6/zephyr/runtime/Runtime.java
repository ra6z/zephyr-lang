package io.ra6.zephyr.runtime;

import io.ra6.zephyr.codeanalysis.binding.scopes.BoundProgramScope;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class Runtime {
    private final List<RegisteredProgram> registeredPrograms = new ArrayList<>();
    @Setter
    @Getter
    private RegisteredProgram mainProgram;

    private final HashMap<BoundProgramScope, List<RuntimeType>> runtimeTypes = new HashMap<>();

    private boolean isProgramRegistered(BoundProgramScope program) {
        return registeredPrograms.stream().anyMatch(p -> p.getProgram().getName().equals(program.getName()));
    }

    public void registerProgram(BoundProgramScope program) {
        if (isProgramRegistered(program)) {
            RuntimeLogger.debugf("Program %s is already registered.", program.getName());
            return;
        }

        program.getImportedPrograms().forEach(this::registerProgram);

        RegisteredProgram registeredProgram = new RegisteredProgram(program, new ProgramInterpreter(this, program));
        registeredPrograms.add(registeredProgram);
        RuntimeLogger.debugf("Registered program: %s", program.getName());
    }

    public RuntimeType getRuntimeType(BoundProgramScope program, TypeSymbol type) {
        if (!isProgramRegistered(program)) {
            RuntimeLogger.debugf("Program %s is not registered.", program.getName());
            return null;
        }

        if(!program.isTypeDeclared(type.getName())){
            RuntimeLogger.tracef("Type %s is not declared in program %s.", type.getName(), program.getName());
            return null;
        }

        if (!runtimeTypes.containsKey(program)) {
            runtimeTypes.put(program, new ArrayList<>());
        }

        List<RuntimeType> types = runtimeTypes.get(program);
        for (RuntimeType runtimeType : types) {
            if (runtimeType.getType().equals(type)) {
                return runtimeType;
            }
        }

        RuntimeType runtimeType = new RuntimeType(this, type, program.getTypeScope(type));
        types.add(runtimeType);
        return runtimeType;

    }

    public RegisteredProgram getProgram(String name) {
        return registeredPrograms.stream().filter(p -> p.getProgram().getName().equals(name)).findFirst().orElse(null);
    }

    public RuntimeType findRuntimeType(TypeSymbol type) {
        for (RegisteredProgram registeredProgram : registeredPrograms) {
            RuntimeType runtimeType = getRuntimeType(registeredProgram.getProgram(), type);
            if (runtimeType != null) {
                return runtimeType;
            }
        }

        return null;
    }

    public ProgramInterpreter findRuntimeEvaluator(TypeSymbol type) {
        for (RegisteredProgram registeredProgram : registeredPrograms) {
            RuntimeType runtimeType = getRuntimeType(registeredProgram.getProgram(), type);
            if (runtimeType != null) {
                return registeredProgram.getEvaluator();
            }
        }

        return null;
    }
}
