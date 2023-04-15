package io.ra6.zephyr.runtime;

import io.ra6.zephyr.builtin.BuiltinTypes;
import io.ra6.zephyr.codeanalysis.binding.Binder;
import io.ra6.zephyr.codeanalysis.binding.BoundProgram;
import io.ra6.zephyr.codeanalysis.binding.ExportSymbol;
import io.ra6.zephyr.codeanalysis.binding.statements.BoundBlockStatement;
import io.ra6.zephyr.codeanalysis.symbols.ArrayTypeSymbol;
import io.ra6.zephyr.codeanalysis.symbols.FunctionSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.symbols.VariableSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxTree;
import io.ra6.zephyr.compiling.CompilerFlags;
import io.ra6.zephyr.library.ZephyrLibrary;
import io.ra6.zephyr.library.ZephyrLibraryMetadata;
import io.ra6.zephyr.sourcefile.SourceText;
import io.ra6.zephyr.writer.DiagnosticWriter;
import io.ra6.zephyr.writer.SyntaxWriter;
import lombok.experimental.ExtensionMethod;

import java.io.IOException;
import java.util.List;

@ExtensionMethod({DiagnosticWriter.class,  SyntaxWriter.class})
public final class Evaluator {
    private final String[] args;

    private final ProgramEvaluator mainProgramEvaluator;

    private Evaluator(BoundProgram boundProgram, String[] args) {
        this.args = args;
        this.mainProgramEvaluator = new ProgramEvaluator(boundProgram.getProgramScope());
    }
    public static EvaluationResult run(String inputPath, String standardLibraryPath, int flags, String[] args) {
        try {
            ZephyrLibraryMetadata standardLibraryMeta = new ZephyrLibraryMetadata("Standard Library", "std", standardLibraryPath, "0.0.1", "rasix", "");
            ZephyrLibrary standardLibrary = new ZephyrLibrary(standardLibraryMeta);

            SourceText mainSourceText = SourceText.fromFile(inputPath);
            SyntaxTree mainTree = SyntaxTree.parse(mainSourceText);

            if (CompilerFlags.isFlagSet(flags, CompilerFlags.PRINT_TREE)) System.out.printTree(mainTree);
            if (CompilerFlags.isFlagSet(flags, CompilerFlags.VERBOSE)) RuntimeLogger.DEBUG = true;

            Binder binder = new Binder(mainTree, standardLibrary);
            BoundProgram boundProgram = binder.bindProgram();

            return Evaluator.evaluate(boundProgram, args);
        } catch (IOException e) {
            return new EvaluationResult(-1);
        }
    }
    private static EvaluationResult evaluate(BoundProgram boundProgram, String[] args) {
        Evaluator evaluator = new Evaluator(boundProgram, args);

        if (boundProgram.getDiagnostics().hasErrors()) {
            System.out.printDiagnostics(boundProgram.getDiagnostics());
            return new EvaluationResult(-1);
        }

        if (boundProgram.getExports().isEmpty()) {
            System.out.println("No exports found. Need at least one export to run the program.");
            return new EvaluationResult(-1);
        }

        ExportSymbol export = boundProgram.getExports().get(0);
        TypeSymbol type = export.getType();

        if (!type.isFieldOrFunctionDeclared("main") || !type.isFunction("main")) {
            System.out.println("No main function found. Need a main function to run the program.");
            return new EvaluationResult(-1);
        }

        FunctionSymbol main = type.getFunction("main", true);

        if (!main.isShared()) {
            System.out.println("Main function is not shared. Need a shared main function to run the program.");
            return new EvaluationResult(-1);
        }

        boolean hasArgs = false;

        if (main.getParameters().size() == 2) {
            if (main.getParameters().get(0).getType().equals(BuiltinTypes.INT) &&
                    main.getParameters().get(1).getType().equals(new ArrayTypeSymbol(BuiltinTypes.STRING))) {
                hasArgs = true;
            } else {
                System.out.println("Main function has invalid parameters. Need a main function with parameters (int, str[]) or no parameters to run the program.");
                return new EvaluationResult(-1);
            }
        } else if (main.getParameters().size() != 0) {
            System.out.println("Main function has invalid parameters. Need a main function with parameters (int, str[]) or no parameters to run the program.");
            return new EvaluationResult(-1);
        }

        // TODO: args
        Object result = evaluator.evaluateEntryMethod(type, main);

        if (result instanceof Integer) return new EvaluationResult((Integer) result);
        return new EvaluationResult(0);
    }

    private Object evaluateEntryMethod(TypeSymbol entryType, FunctionSymbol mainFunction) {
        mainProgramEvaluator.getLocals().push(new VariableTable("<entry>"));

        VariableSymbol argcSymbol = new VariableSymbol("argc", true, BuiltinTypes.INT);
        // TODO: add array
        VariableSymbol argvSymbol = new VariableSymbol("argv", true, new ArrayTypeSymbol(BuiltinTypes.STRING));

        mainProgramEvaluator.assign(argcSymbol, args.length);
        mainProgramEvaluator.assign(argvSymbol, List.of(args));

        BoundBlockStatement mainFunctionBody = mainProgramEvaluator.getBoundProgramScope().getTypeScope(entryType).getFunctionScope(mainFunction);
        mainProgramEvaluator.evaluateStatement(mainFunctionBody);
        mainProgramEvaluator.getLocals().pop();

        if (mainFunction.getType() != BuiltinTypes.VOID) return mainProgramEvaluator.getLastValue();
        return null;
    }

}
