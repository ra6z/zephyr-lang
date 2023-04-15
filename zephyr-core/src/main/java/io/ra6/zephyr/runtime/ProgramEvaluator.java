package io.ra6.zephyr.runtime;

import io.ra6.zephyr.builtin.InternalBinaryOperator;
import io.ra6.zephyr.builtin.InternalFunction;
import io.ra6.zephyr.builtin.InternalUnaryOperator;
import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.codeanalysis.binding.*;
import io.ra6.zephyr.codeanalysis.binding.expressions.*;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundProgramScope;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.codeanalysis.binding.statements.*;
import io.ra6.zephyr.codeanalysis.symbols.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProgramEvaluator {
    @Getter
    private final BoundProgramScope boundProgramScope;

    @Getter
    private Object lastValue;

    @Getter
    private final VariableTableStack locals = new VariableTableStack();

    private final List<ProgramEvaluator> importedPrograms = new ArrayList<>();

    public ProgramEvaluator(BoundProgramScope boundProgramScope) {
        this.boundProgramScope = boundProgramScope;

        for (BoundProgramScope importedProgram : boundProgramScope.getImportedPrograms()) {
            importedPrograms.add(new ProgramEvaluator(importedProgram));
        }
    }

    private ProgramEvaluator getProgramEvaluator(BoundProgramScope programScope) {
        if (boundProgramScope == programScope) {
            return this;
        }

        RuntimeLogger.log("Looking for program: " + boundProgramScope.getDebugImportedProgram(programScope));

        for (ProgramEvaluator importedProgram : importedPrograms) {
            if (importedProgram.getBoundProgramScope() == programScope) {
                return importedProgram;
            }
        }

        throw new RuntimeException("Program not found.");
    }


    private void evaluateExpressionStatement(BoundExpressionStatement s) {
        lastValue = evaluateExpression(s.getExpression());
    }


    protected Object evaluateStatement(BoundBlockStatement body) {
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
            case FUNCTION_CALL_EXPRESSION -> evaluateFunctionCallExpression((BoundFunctionCallExpression) expression);
            case MEMBER_ACCESS_EXPRESSION -> evaluateMemberAccessExpression((BoundMemberAccessExpression) expression);
            case FIELD_ACCESS_EXPRESSION -> evaluateFieldAccessExpression((BoundFieldAccessExpression) expression);
            case INSTANCE_CREATION_EXPRESSION ->
                    evaluateInstanceCreationExpression((BoundInstanceCreationExpression) expression);
            case ARRAY_LITERAL_EXPRESSION -> evaluateArrayLiteralExpression((BoundArrayLiteralExpression) expression);
            case THIS_EXPRESSION -> evaluateThisExpression((BoundThisExpression) expression);
            case TYPE_EXPRESSION -> evaluateTypeExpression((BoundTypeExpression) expression);
            case INTERNAL_FUNCTION_EXPRESSION ->
                    evaluateInternalFunctionExpression((BoundInternalFunctionExpression) expression);
            case ERROR_EXPRESSION -> throw new RuntimeException("Error expression");
            case ARRAY_ACCESS_EXPRESSION -> evaluateArrayAccessExpression((BoundArrayAccessExpression) expression);
            case ARRAY_CREATION_EXPRESSION ->
                    evaluateArrayCreationExpression((BoundArrayCreationExpression) expression);
            case CONVERSION_EXPRESSION -> evaluateConversionExpression((BoundConversionExpression) expression);
            default -> throw new RuntimeException("Unexpected expression kind: " + expression.getKind());
        };
    }

    private Object evaluateConversionExpression(BoundConversionExpression expression) {
        Object value = evaluateExpression(expression.getExpression());
        return convert(value, expression.getType());
    }

    private Object convert(Object value, TypeSymbol type) {

        return value;
    }

    private static Object[] createMultiDimensionalArray(List<Integer> dimensions, int depth) {
        Object[] result = new Object[dimensions.get(0)];
        if (depth == 1) {
            for (int i = 0; i < dimensions.get(0); i++) {
                result[i] = null;
            }
        } else {
            for (int i = 0; i < dimensions.get(0); i++) {
                List<Integer> subDimensions = dimensions.subList(1, dimensions.size());
                result[i] = createMultiDimensionalArray(subDimensions, depth - 1);
            }
        }
        return result;
    }

    private Object evaluateArrayCreationExpression(BoundArrayCreationExpression expression) {
        List<Integer> dimensions = new ArrayList<>();

        for (int i = 0; i < expression.getDimensions().size(); i++) {
            Object value = evaluateExpression(expression.getDimensions().get(i));

            if (!(value instanceof Integer valueInt)) {
                throw new RuntimeException("Array dimension must be an integer");
            }

            if (valueInt < 0) {
                throw new RuntimeException("Array dimension must be positive");
            }

            dimensions.add(valueInt);
        }

        return createMultiDimensionalArray(dimensions, dimensions.size());
    }

    private Object evaluateArrayAccessExpression(BoundArrayAccessExpression expression) {
        Object array = evaluateExpression(expression.getTarget());
        Object index = evaluateExpression(expression.getIndex());

        if (!(array instanceof Object[])) {
            throw new RuntimeException("Cannot access array element of non-array type");
        }

        if (!(index instanceof Integer)) {
            throw new RuntimeException("Array index must be an integer");
        }

        if ((Integer) index >= ((Object[]) array).length) {
            throw new RuntimeException("Array index out of bounds");
        }

        return ((Object[]) array)[(Integer) index];
    }

    private Object evaluateArrayLiteralExpression(BoundArrayLiteralExpression expression) {
        List<Object> values = new ArrayList<>();

        for (BoundExpression e : expression.getElements()) {
            values.add(evaluateExpression(e));
        }

        return values.toArray();
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

        if ((expression.getFunction() instanceof InternalFunction internalFunction && !internalFunction.isShared()) || expression.getFunction() instanceof InternalBinaryOperator || expression.getFunction() instanceof InternalUnaryOperator) {
            localVariables.put("this", locals.peek().get(locals.peek().keySet().stream().filter(s -> s.getName().equals("this")).findFirst().orElseThrow()));
        }

        for (int i = 0; i < expression.getFunction().getArity(); i++) {
            ParameterSymbol parameter = expression.getFunction().getParameters().get(i);
            localVariables.put(parameter.getName(), arguments.get(i));
        }

        return expression.getFunction().getFunctionBody().call(localVariables);
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

        locals.push(new VariableTable("binary-body (" + leftType.getName() + " " + operator + " " + rightType.getName() + ")"));

        VariableSymbol thisSymbol = new VariableSymbol("this", true, expression.getLeft().getType());
        assign(thisSymbol, thisValue);

        VariableSymbol rightSymbol = new VariableSymbol(binaryOperator.getOtherOperandName(), true, binaryOperator.getOtherType());
        assign(rightSymbol, right);

        BoundBlockStatement binaryBody = boundProgramScope.getTypeScope(leftType).getBinaryOperatorScope(binaryOperator);
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
        locals.push(new VariableTable("unary-body (" + operator + " " + type.getName() + ")"));

        VariableSymbol thisSymbol = new VariableSymbol("this", true, expression.getType());
        assign(thisSymbol, thisValue);

        BoundBlockStatement unaryBody = boundProgramScope.getTypeScope(type).getUnaryOperatorScope(unaryOperator);
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
        }

        if (target instanceof BoundArrayAccessExpression arrayAccessExpression) {
            Object array = evaluateExpression(arrayAccessExpression.getTarget());
            Object index = evaluateExpression(arrayAccessExpression.getIndex());
            Object value = evaluateExpression(expression.getExpression());

            if (!(array instanceof Object[])) {
                throw new RuntimeException("Cannot access array element of non-array type");
            }

            if (!(index instanceof Integer)) {
                throw new RuntimeException("Array index must be an integer");
            }

            // check index out of bounds
            if ((Integer) index >= ((Object[]) array).length) {
                throw new RuntimeException("Array index out of bounds");
            }

            ((Object[]) array)[(Integer) index] = value;
            return value;
        }

        throw new RuntimeException("Cannot assign to expression of type " + target.getClass().getSimpleName());
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

                Object value = evaluateExpression(boundProgramScope.getTypeScope(type).getFieldInitializer(field));
                if (value == null)
                    throw new RuntimeException("Field " + field.getName() + " is not initialized in type " + type.getName());
                return value;
            }
        }

        throw new RuntimeException("Unexpected target type: " + target.getClass().getSimpleName());
    }

    private Object evaluateFunctionCallExpression(BoundFunctionCallExpression expression) {
        BoundExpression callee = expression.getCallee();

        // check if the callee is a type instance
        if (callee instanceof BoundVariableExpression variableExpression) {
            VariableSymbol variable = variableExpression.getVariable();

            // check if variable type is builtin type
            if (Types.isBuiltinType(variable.getType())) {
                return evaluateBuiltinFunctionCall(callee, variable.getType(), expression.getFunction(), expression.getArguments());
            }
        }

        Object calleeValue = evaluateExpression(expression.getCallee());


        if (calleeValue instanceof TypeInstance instance) {
            return evaluateInstanceMethodCall(instance, expression.getFunction(), expression.getArguments());
        } else if (calleeValue instanceof TypeSymbol type) {
            return evaluateSharedFunctionCall(type, expression.getFunction(), expression.getArguments());
        }

        if (callee instanceof BoundLiteralExpression literalExpression) {
            if (Types.isValidLiteralType(literalExpression.getType())) {
                return evaluateBuiltinFunctionCall(callee, literalExpression.getType(), expression.getFunction(), expression.getArguments());
            }
        }

        if (Types.isValidLiteralType(calleeValue.getClass())) {
            return evaluateBuiltinFunctionCall(callee, Types.getLiteralType(calleeValue.getClass()), expression.getFunction(), expression.getArguments());
        }

        if (calleeValue instanceof Object[]) {
            FunctionSymbol function = expression.getFunction();

            if (function.getName().equals("copy")) {
                return ((Object[]) calleeValue).clone();
            }
        }

        throw new RuntimeException("Unexpected callee type: " + calleeValue.getClass().getSimpleName());
    }

    private Object evaluateBuiltinFunctionCall(BoundExpression callee, TypeSymbol type, FunctionSymbol function, List<BoundExpression> arguments) {
        List<Object> evaluatedArguments = new ArrayList<>();
        for (BoundExpression argument : arguments) {
            evaluatedArguments.add(evaluateExpression(argument));
        }

        Object calleeValue = evaluateExpression(callee);

        locals.push(new VariableTable("builtin-function (" + type.getName() + "." + function.getName() + ")"));
        assign(new VariableSymbol("this", true, type), calleeValue);

        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            Object value = evaluatedArguments.get(i);

            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            assign(variable, value);
        }

        BoundBlockStatement functionBody = boundProgramScope.getTypeScope(type).getFunctionScope(function);
        Object result = evaluateStatement(functionBody);
        locals.pop();

        return result;
    }

    private Object evaluateInstanceMethodCallEvaluated(TypeInstance instance, FunctionSymbol function, List<Object> arguments) {
        locals.push(new VariableTable("function (" + function.getName() + ")"));
        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            Object value = arguments.get(i);

            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            assign(variable, value);
        }

        assign(new VariableSymbol("this", true, instance.getType()), instance);
        BoundBlockStatement functionBody = boundProgramScope.getTypeScope(instance.getType()).getFunctionScope(function);
        Object result = evaluateStatement(functionBody);
        locals.pop();

        if (function.getType() == Types.VOID) return null;
        return result;
    }

    private Object evaluateInstanceMethodCall(TypeInstance instance, FunctionSymbol function, List<BoundExpression> arguments) {
        if (function.isShared()) {
            throw new RuntimeException("Cannot call shared function from instance context");
        }

        if (boundProgramScope.isTypeImported(instance.getType())) {
            ProgramEvaluator evaluator = getProgramEvaluator(boundProgramScope.getImportedProgram(instance.getType().getName()));

            List<Object> evaluatedArguments = new ArrayList<>();
            for (BoundExpression argument : arguments) {
                evaluatedArguments.add(evaluateExpression(argument));
            }

            return evaluator.evaluateInstanceMethodCallEvaluated(instance, function, evaluatedArguments);
        }

        List<Object> evaluatedArguments = new ArrayList<>();
        for (BoundExpression argument : arguments) {
            evaluatedArguments.add(evaluateExpression(argument));
        }

        locals.push(new VariableTable("function (" + function.getName() + ")"));
        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            Object value = evaluatedArguments.get(i);

            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            assign(variable, value);
        }

        assign(new VariableSymbol("this", true, instance.getType()), instance);
        BoundBlockStatement functionBody = boundProgramScope.getTypeScope(instance.getType()).getFunctionScope(function);
        Object result = evaluateStatement(functionBody);
        locals.pop();

        if (function.getType() == Types.VOID) return null;
        return result;
    }

    private Object evaluateSharedFunctionCall(TypeSymbol type, FunctionSymbol function, List<BoundExpression> arguments) {
        if (!function.isShared()) {
            throw new RuntimeException("Cannot call non-shared function from static context");
        }

        if (boundProgramScope.isTypeImported(type)) {
            ProgramEvaluator evaluator = getProgramEvaluator(boundProgramScope.getImportedProgram(type.getName()));

            List<Object> evaluatedArguments = new ArrayList<>();
            for (BoundExpression argument : arguments) {
                evaluatedArguments.add(evaluateExpression(argument));
            }

            return evaluator.evaluateSharedFunctionCallEvaluated(type, function, evaluatedArguments);
        }

        List<Object> evaluatedArguments = new ArrayList<>();
        for (BoundExpression argument : arguments) {
            evaluatedArguments.add(evaluateExpression(argument));
        }

        locals.push(new VariableTable("shared-function (" + type.getName() + "." + function.getName() + ")"));
        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            Object value = evaluatedArguments.get(i);

            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            assign(variable, value);
        }

        BoundTypeScope typeScope = boundProgramScope.getTypeScope(type);
        BoundBlockStatement functionBody = typeScope.getFunctionScope(function);

        Object result = evaluateStatement(functionBody);
        locals.pop();

        return result;
    }

    private Object evaluateSharedFunctionCallEvaluated(TypeSymbol type, FunctionSymbol function, List<Object> evaluatedArguments) {
        locals.push(new VariableTable("shared-function (" + type.getName() + "." + function.getName() + ")"));
        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            Object value = evaluatedArguments.get(i);

            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            assign(variable, value);
        }

        BoundTypeScope typeScope = boundProgramScope.getTypeScope(type);
        BoundBlockStatement functionBody = typeScope.getFunctionScope(function);

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

                Object value = evaluateExpression(boundProgramScope.getTypeScope(type).getFieldInitializer(field));
                if (value == null)
                    throw new RuntimeException("Field " + field.getName() + " is not initialized in type " + type.getName());
                return value;
            }
        }

        if (expression.getTarget().getType() instanceof ArrayTypeSymbol) {
            if (expression.getMember() instanceof FieldSymbol field) {
                if (field.getName().equals("length")) {
                    return ((List<?>) instanceValue).size();
                }
            }
        }

        if (!(instanceValue instanceof TypeInstance instance)) {
            throw new RuntimeException("Cannot access member of non-instance");
        }

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
        if (boundProgramScope.isTypeImported(expression.getType().getName())) {
            // Create new Program evaluator and evaluate the imported program
            ProgramEvaluator evaluator = getProgramEvaluator(boundProgramScope.getImportedProgram(expression.getType().getName()));
            return evaluator.evaluateInstanceCreationExpression(expression);
        }

        BoundTypeScope typeScope = boundProgramScope.getTypeScope(expression.getType());
        // create instance variables
        locals.push(new VariableTable("instance (" + expression.getType().getName() + ")"));

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

        locals.push(new VariableTable("constructor (" + expression.getType().getName() + ")"));
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

    protected void assign(VariableSymbol variableSymbol, Object value) {
        RuntimeLogger.printf("%s: %s -> %s%n", variableSymbol.getName(), variableSymbol.getType(), value);
        locals.peek().put(variableSymbol, value);
    }
}
