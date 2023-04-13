package io.ra6.zephyr.evaluating;

import io.ra6.zephyr.codeanalysis.binding.*;
import io.ra6.zephyr.codeanalysis.binding.expressions.*;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.codeanalysis.binding.statements.*;
import io.ra6.zephyr.builtin.BuiltinTypes;
import io.ra6.zephyr.builtin.InternalBinaryOperator;
import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.builtin.InternalUnaryOperator;
import io.ra6.zephyr.codeanalysis.symbols.*;
import io.ra6.zephyr.runtime.TypeInstance;
import io.ra6.zephyr.runtime.VariableTable;
import io.ra6.zephyr.runtime.VariableTableStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Evaluator {
    private final BoundProgram boundProgram;
    private final String[] args;

    private Object lastValue;
    private final VariableTableStack locals = new VariableTableStack();

    private Evaluator(BoundProgram boundProgram, String[] args) {
        this.boundProgram = boundProgram;
        this.args = args;
    }

    public static EvaluationResult evaluate(BoundProgram boundProgram, String[] args) {
        Evaluator evaluator = new Evaluator(boundProgram, args);

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
            if (main.getParameters().get(0).getType() == BuiltinTypes.INT && main.getParameters().get(1).getType() == BuiltinTypes.STRING) {
                hasArgs = true;
            } else {
                System.out.println("Main function has invalid parameters. Need a main function with parameters (int, string[]) or no parameters to run the program.");
                return new EvaluationResult(-1);
            }
        } else if (main.getParameters().size() != 0) {
            System.out.println("Main function has invalid parameters. Need a main function with parameters (int, string[]) or no parameters to run the program.");
            return new EvaluationResult(-1);
        }

        // TODO: args
        Object result = evaluator.evaluateEntryMethod(type, main);

        if (result instanceof Integer) return new EvaluationResult((Integer) result);
        return new EvaluationResult(0);
    }

    private Object evaluateEntryMethod(TypeSymbol entryType, FunctionSymbol mainFunction) {
        locals.push(new VariableTable("<entry>"));

        VariableSymbol argcSymbol = new VariableSymbol("argc", true, BuiltinTypes.INT);
        // TODO: add array
        VariableSymbol argvSymbol = new VariableSymbol("argv", true, BuiltinTypes.STRING);

        assign(argcSymbol, args.length);
        assign(argvSymbol, String.join(" ", args));

        BoundBlockStatement mainFunctionBody = boundProgram.getProgramScope().getTypeScope(entryType).getFunctionScope(mainFunction);
        evaluateStatement(mainFunctionBody);
        locals.pop();

        if (mainFunction.getType() != BuiltinTypes.VOID) return lastValue;

        return null;
    }

    private Object evaluateStatement(BoundBlockStatement body) {
        HashMap<BoundLabel, Integer> labelToIndex = new HashMap<>();

        for (int i = 0; i < body.getStatements().size(); i++) {
            if (body.getStatements().get(i) instanceof BoundLabelStatement label) {
                labelToIndex.put(label.getLabel(), i);
            }
        }

        int index = 0;

        while (index < body.getStatements().size()) {
            BoundStatement s = body.getStatements().get(index);

            switch (s.getKind()) {
                case VARIABLE_DECLARATION -> {
                    evaluateVariableDeclaration((BoundVariableDeclaration) s);
                    index++;
                }
                case EXPRESSION_STATEMENT -> {
                    evaluateExpressionStatement((BoundExpressionStatement) s);
                    index++;
                }
                case GOTO_STATEMENT -> {
                    BoundGotoStatement gs = (BoundGotoStatement) s;
                    index = labelToIndex.get(gs.getLabel());
                }
                case CONDITIONAL_GOTO_STATEMENT -> {
                    BoundConditionalGotoStatement cgs = (BoundConditionalGotoStatement) s;
                    boolean condition = (boolean) evaluateExpression(cgs.getCondition());

                    if (condition == cgs.jumpIfTrue()) {
                        index = labelToIndex.get(cgs.getLabel());
                    } else {
                        index++;
                    }
                }
                case LABEL_STATEMENT -> {
                    index++;
                }
                case RETURN_STATEMENT -> {
                    BoundReturnStatement rs = (BoundReturnStatement) s;
                    lastValue = rs.getExpression() == null ? null : evaluateExpression(rs.getExpression());
                    index++;
                    return lastValue;
                }
                default -> throw new RuntimeException("Unexpected statement kind: " + s.getKind());
            }
        }

        return lastValue;
    }

    private void evaluateExpressionStatement(BoundExpressionStatement s) {
        lastValue = evaluateExpression(s.getExpression());
    }


    private void evaluateVariableDeclaration(BoundVariableDeclaration s) {
        Object value = evaluateExpression(s.getInitializer());

        lastValue = value;
        assign(s.getVariableSymbol(), value);
    }

    private Object evaluateExpression(BoundExpression expression) {
        if (expression instanceof BoundLiteralExpression literal) {
            return evaluateLiteralExpression(literal);
        }
        return switch (expression.getKind()) {
            case VARIABLE_EXPRESSION -> evaluateVariableExpression((BoundVariableExpression) expression);
            case ASSIGNMENT_EXPRESSION -> evaluateAssignmentExpression((BoundAssignmentExpression) expression);
            case UNARY_EXPRESSION -> evaluateUnaryExpression((BoundUnaryExpression) expression);
            case BINARY_EXPRESSION -> evaluateBinaryExpression((BoundBinaryExpression) expression);
            case CONDITIONAL_EXPRESSION -> evaluateConditionalExpression((BoundConditionalExpression) expression);
            case METHOD_CALL_EXPRESSION -> evaluateMethodCallExpression((BoundMethodCallExpression) expression);
            case MEMBER_ACCESS_EXPRESSION -> evaluateMemberAccessExpression((BoundMemberAccessExpression) expression);
            case FIELD_ACCESS_EXPRESSION -> evaluateFieldAccessExpression((BoundFieldAccessExpression) expression);
            case INSTANCE_CREATION_EXPRESSION ->
                    evaluateInstanceCreationExpression((BoundInstanceCreationExpression) expression);
            case THIS_EXPRESSION -> evaluateThisExpression((BoundThisExpression) expression);
            case TYPE_EXPRESSION -> evaluateTypeExpression((BoundTypeExpression) expression);
            case INTERNAL_FUNCTION_EXPRESSION ->
                    evaluateInternalFunctionExpression((BoundInternalFunctionExpression) expression);
            case ERROR_EXPRESSION -> throw new RuntimeException("Error expression");
            default -> throw new RuntimeException("Unexpected expression kind: " + expression.getKind());
        };
    }

    private Object evaluateInternalFunctionExpression(BoundInternalFunctionExpression expression) {
        List<Object> arguments = new ArrayList<>();
        for (BoundExpression arg : expression.getArguments()) {
            Object value = evaluateExpression(arg);
            arguments.add(value);
        }

        if (arguments.size() != expression.getFunction().getArity()) {
            throw new RuntimeException("Invalid number of arguments for internal function " + expression.getFunction());
        }

        HashMap<String, Object> localVariables = new HashMap<>();

        if ((expression.getFunction() instanceof InternalFunction internalFunction && !internalFunction.isShared()) ||
                expression.getFunction() instanceof InternalBinaryOperator ||
                expression.getFunction() instanceof InternalUnaryOperator) {
            localVariables.put("this", locals.peek().get(locals.peek().keySet().stream().filter(s -> s.getName().equals("this")).findFirst().orElseThrow()));
        }

        for (int i = 0; i < expression.getFunction().getArity(); i++) {
            ParameterSymbol parameter = expression.getFunction().getParameters().get(i);
            localVariables.put(parameter.getName(), arguments.get(i));
        }

        return expression.getFunction().getFunctionBody().invoke(localVariables);
    }

    private Object evaluateBinaryExpression(BoundBinaryExpression expression) {
        TypeSymbol leftType = expression.getLeft().getType();
        TypeSymbol rightType = expression.getRight().getType();

        Object thisValue = evaluateExpression(expression.getLeft());
        Object right = evaluateExpression(expression.getRight());

        String operator = expression.getOperator();

        BinaryOperatorSymbol binaryOperator = leftType.getBinaryOperator(operator, rightType);

        if (binaryOperator == null)
            throw new RuntimeException("No binary operator " + operator + " found for types " + leftType.getName() + " and " + rightType.getName());

        locals.push(new VariableTable("binary-body"));

        VariableSymbol thisSymbol = new VariableSymbol("this", true, expression.getLeft().getType());
        assign(thisSymbol, thisValue);

        VariableSymbol rightSymbol = new VariableSymbol(binaryOperator.getOtherOperandName(), true, binaryOperator.getOtherType());
        assign(rightSymbol, right);

        BoundBlockStatement binaryBody = boundProgram.getProgramScope().getTypeScope(leftType).getBinaryOperatorScope(binaryOperator);
        Object result = evaluateStatement(binaryBody);
        locals.pop();

        return result;
    }

    private Object evaluateUnaryExpression(BoundUnaryExpression expression) {
        String operator = expression.getOperator();
        TypeSymbol type = expression.getType();
        UnaryOperatorSymbol unaryOperator = type.getUnaryOperator(operator);

        if (unaryOperator == null)
            throw new RuntimeException("No unary operator " + operator + " found for type " + type.getName());

        Object thisValue = evaluateExpression(expression.getOperand());
        locals.push(new VariableTable("unary-body"));

        VariableSymbol thisSymbol = new VariableSymbol("this", true, expression.getType());
        assign(thisSymbol, thisValue);

        BoundBlockStatement unaryBody = boundProgram.getProgramScope().getTypeScope(type).getUnaryOperatorScope(unaryOperator);
        Object result = evaluateStatement(unaryBody);
        locals.pop();

        return result;
    }

    private Object evaluateAssignmentExpression(BoundAssignmentExpression expression) {
        BoundExpression target = expression.getTarget();

        if (target instanceof BoundMemberAccessExpression memberAccessExpression) {
            Object callee = evaluateExpression(memberAccessExpression.getTarget());
            Symbol member = memberAccessExpression.getMember();

            if (!(callee instanceof TypeInstance instance))
                throw new RuntimeException("Cannot access member of non-object type " + callee.getClass().getSimpleName());

            Object value = evaluateExpression(expression.getExpression());
            FieldSymbol field = instance.getType().getField(member.getName());

            instance.setField(field, value);
            return value;
        }

        if (target instanceof BoundVariableExpression variableExpression) {
            Object value = evaluateExpression(expression.getExpression());
            assign(variableExpression.getVariable(), value);
            return value;
        } else {
            throw new RuntimeException("Cannot assign to expression of type " + target.getClass().getSimpleName());
        }
    }

    private Object evaluateVariableExpression(BoundVariableExpression expression) {
        return locals.peek().keySet().stream().filter(s -> s.getName().equals(expression.getVariable().getName()) && s.isReadonly() == expression.getVariable().isReadonly() && s.getType().equals(expression.getVariable().getType())).findFirst().map(locals.peek()::get).orElseThrow(() -> new RuntimeException("Variable " + expression.getVariable().getName() + " not found"));
    }

    private Object evaluateThisExpression(BoundThisExpression expression) {
        VariableSymbol thisSymbol = locals.peek().keySet().stream().filter(s -> s.getName().equals("this")).findFirst().orElse(null);

        if (thisSymbol == null) throw new RuntimeException("Cannot access this outside of instance context");

        return locals.peek().get(thisSymbol);
    }

    private Object evaluateFieldAccessExpression(BoundFieldAccessExpression expression) {
        Object target = evaluateExpression(expression.getTarget());
        FieldSymbol field = expression.getField();

        if (target instanceof TypeInstance instance) {
            if (field.isShared()) {
                throw new RuntimeException("Cannot access shared field from instance context");
            } else {
                return instance.getFieldValue(field);
            }
        }

        if (target instanceof TypeSymbol type) {
            if (!field.isShared()) {
                throw new RuntimeException("Cannot access instance field from shared context");
            } else {
                if (!type.isFieldOrFunctionDeclared(field.getName())) {
                    throw new RuntimeException("Field " + field.getName() + " is not declared in type " + type.getName());
                }

                if (!type.isField(field.getName())) {
                    throw new RuntimeException("Field " + field.getName() + " is not a field in type " + type.getName());
                }

                Object value = evaluateExpression(boundProgram.getProgramScope().getTypeScope(type).getFieldInitializer(field));
                if (value == null)
                    throw new RuntimeException("Field " + field.getName() + " is not initialized in type " + type.getName());
                return value;
            }
        }

        throw new RuntimeException("Unexpected target type: " + target.getClass().getSimpleName());
    }

    private Object evaluateMethodCallExpression(BoundMethodCallExpression expression) {
        BoundExpression callee = expression.getCallee();

        // check if the callee is a type instance
        if (callee instanceof BoundVariableExpression variableExpression) {
            VariableSymbol variable = variableExpression.getVariable();

            // check if variable type is builtin type
            if (BuiltinTypes.isBuiltinType(variable.getType())) {
                return evaluateBuiltinFunctionCall(callee, variable.getType(), expression.getFunction(), expression.getArguments());
            }
        }

        Object calleeValue = evaluateExpression(expression.getCallee());

        if (calleeValue instanceof TypeInstance instance) {
            return evaluateInstanceMethodCall(instance, expression.getFunction(), expression.getArguments());
        } else if (calleeValue instanceof TypeSymbol type) {
            return evaluateSharedFunctionCall(type, expression.getFunction(), expression.getArguments());
        } else if (calleeValue instanceof FunctionSymbol function) {

        }

        throw new RuntimeException("Unexpected callee type: " + calleeValue.getClass().getSimpleName());
    }

    private Object evaluateBuiltinFunctionCall(BoundExpression callee, TypeSymbol type, FunctionSymbol function, List<BoundExpression> arguments) {
        List<Object> evaluatedArguments = new ArrayList<>();
        for (BoundExpression argument : arguments) {
            evaluatedArguments.add(evaluateExpression(argument));
        }

        Object calleeValue = evaluateExpression(callee);

        locals.push(new VariableTable("builtin-function"));
        assign(new VariableSymbol("this", true, type), calleeValue);

        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            Object value = evaluatedArguments.get(i);

            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            assign(variable, value);
        }

        BoundBlockStatement functionBody = boundProgram.getProgramScope().getTypeScope(type).getFunctionScope(function);
        Object result = evaluateStatement(functionBody);
        locals.pop();

        return result;
    }

    private Object evaluateInstanceMethodCall(TypeInstance instance, FunctionSymbol function, List<BoundExpression> arguments) {
        if (function.isShared()) {
            throw new RuntimeException("Cannot call shared function from instance context");
        }

        List<Object> evaluatedArguments = new ArrayList<>();
        for (BoundExpression argument : arguments) {
            evaluatedArguments.add(evaluateExpression(argument));
        }

        locals.push(new VariableTable("function"));
        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            Object value = evaluatedArguments.get(i);

            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            assign(variable, value);
        }

        assign(new VariableSymbol("this", true, instance.getType()), instance);
        BoundBlockStatement functionBody = boundProgram.getProgramScope().getTypeScope(instance.getType()).getFunctionScope(function);
        Object result = evaluateStatement(functionBody);
        locals.pop();

        if (function.getType() == BuiltinTypes.VOID) return null;
        return result;
    }

    private Object evaluateSharedFunctionCall(TypeSymbol type, FunctionSymbol function, List<BoundExpression> arguments) {
        if (!function.isShared()) {
            throw new RuntimeException("Cannot call non-shared function from static context");
        }

        List<Object> evaluatedArguments = new ArrayList<>();
        for (BoundExpression argument : arguments) {
            evaluatedArguments.add(evaluateExpression(argument));
        }

        locals.push(new VariableTable("shared-function"));
        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            Object value = evaluatedArguments.get(i);

            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            assign(variable, value);
        }

        BoundBlockStatement functionBody = boundProgram.getProgramScope().getTypeScope(type).getFunctionScope(function);

        Object result = evaluateStatement(functionBody);
        locals.pop();

        return result;
    }

    private Object evaluateMemberAccessExpression(BoundMemberAccessExpression expression) {
        Object instanceValue = evaluateExpression(expression.getTarget());

        if (instanceValue instanceof TypeSymbol type) {
            if (expression.getMember() instanceof FieldSymbol field) {
                if (!type.isFieldOrFunctionDeclared(field.getName())) {
                    throw new RuntimeException("Field " + field.getName() + " is not declared in type " + type.getName());
                }

                if (!type.isField(field.getName())) {
                    throw new RuntimeException("Field " + field.getName() + " is not a field in type " + type.getName());
                }

                Object value = evaluateExpression(boundProgram.getProgramScope().getTypeScope(type).getFieldInitializer(field));
                if (value == null)
                    throw new RuntimeException("Field " + field.getName() + " is not initialized in type " + type.getName());
                return value;
            }
        }

        if (!(instanceValue instanceof TypeInstance instance)) {
            throw new RuntimeException("Cannot access member of non-instance");
        }

        // TODO: check if function or field can be accessed

        if (expression.getMember() instanceof FieldSymbol field) {
            // check if current type is target type
            return instance.getFieldValue(field);
        }

        throw new RuntimeException("Unexpected member type: " + expression.getMember().getClass().getSimpleName());
    }

    private Object evaluateConditionalExpression(BoundConditionalExpression expression) {
        boolean condition = (boolean) evaluateExpression(expression.getCondition());
        return condition ? evaluateExpression(expression.getThenExpression()) : evaluateExpression(expression.getElseExpression());
    }

    private Object evaluateTypeExpression(BoundTypeExpression expression) {
        return expression.getType();
    }

    private Object evaluateInstanceCreationExpression(BoundInstanceCreationExpression expression) {
        BoundTypeScope typeScope = boundProgram.getProgramScope().getTypeScope(expression.getType());
        // create instance variables
        // TODO: maybe remove?
        locals.push(new VariableTable("instance"));

        for (FieldSymbol field : typeScope.getDeclaredFieldsAndFunctions().stream().filter(s -> s instanceof FieldSymbol).map(s -> (FieldSymbol) s).filter(f -> !f.isShared()).toList()) {
            VariableSymbol variable = new VariableSymbol(field.getName(), field.isReadonly(), field.getType());

            Object value = null;
            if (typeScope.getFieldInitializer(field) != null) {
                value = evaluateExpression(typeScope.getFieldInitializer(field));
            }
            assign(variable, value);
        }

        TypeInstance instance = new TypeInstance(expression.getType(), locals.peek());

        locals.pop();

        ConstructorSymbol constructor = expression.getType().getConstructor(expression.getArguments().size());
        BoundBlockStatement constructorBody = typeScope.getConstructorScope(constructor);

        HashMap<VariableSymbol, Object> arguments = new HashMap<>();
        for (int i = 0; i < expression.getArguments().size(); i++) {
            BoundExpression argument = expression.getArguments().get(i);
            ParameterSymbol parameter = constructor.getParameters().get(i);
            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());

            Object value = evaluateExpression(argument);

            arguments.put(variable, value);
        }

        locals.push(new VariableTable("constructor"));
        assign(new VariableSymbol("this", true, expression.getType()), instance);
        for (VariableSymbol variable : arguments.keySet()) {
            assign(variable, arguments.get(variable));
        }
        evaluateStatement(constructorBody);
        locals.pop();

        return instance;
    }

    private Object evaluateLiteralExpression(BoundLiteralExpression literal) {
        return literal.getValue();
    }

    private void assign(VariableSymbol variableSymbol, Object value) {
        //System.out.printf("%s: %s -> %s%n", variableSymbol.getName(), variableSymbol.getType(), value);
        locals.peek().put(variableSymbol, value);
    }
}
