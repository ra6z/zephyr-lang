package io.ra6.zephyr.runtime;

import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundProgramScope;
import io.ra6.zephyr.codeanalysis.symbols.ExportSymbol;
import io.ra6.zephyr.codeanalysis.symbols.FunctionSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Interpreter {
    @Getter
    private int exitCode;
    private final Runtime runtime;
    private final String[] args;

    public void run() {
        RegisteredProgram mainProgram = runtime.getMainProgram();
        BoundProgramScope program = mainProgram.getProgram();
        ProgramInterpreter evaluator = mainProgram.getEvaluator();

        for (ExportSymbol export : program.getExports()) {
            RuntimeType entryType = evaluator.getRuntimeType(program, export.getType());
            TypeSymbol type = entryType.getType();

            if (type.isFunctionDefined("main")) {
                FunctionSymbol mainFunction = entryType.getFunction("main", true);
                if (mainFunction == null) {
                    RuntimeLogger.debugf("No main function found in program %s%n", program.getName());
                    return;
                }

                if(!mainFunction.getType().equals(Types.INT) && !mainFunction.getType().equals(Types.VOID)) {
                    RuntimeLogger.errorf("Main function must return int or void%n");
                    return;
                }

                boolean returns = mainFunction.getType().equals(Types.INT);

                Object result = evaluator.evaluateFunctionWithEvaluatedArgs(entryType, mainFunction, new Object[]{args});
                if(returns) {
                    exitCode = (int) result;
                    return;
                }

                exitCode = 0;
                return;
            }
        }
    }
}
