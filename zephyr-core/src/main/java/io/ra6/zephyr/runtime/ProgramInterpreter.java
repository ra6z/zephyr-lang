package io.ra6.zephyr.runtime;

import io.ra6.zephyr.Tuple;
import io.ra6.zephyr.builtin.*;
import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundLabel;
import io.ra6.zephyr.codeanalysis.binding.BoundStatement;
import io.ra6.zephyr.codeanalysis.binding.BoundTypeCheckExpression;
import io.ra6.zephyr.codeanalysis.binding.expressions.*;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundProgramScope;
import io.ra6.zephyr.codeanalysis.binding.scopes.BoundTypeScope;
import io.ra6.zephyr.codeanalysis.binding.statements.*;
import io.ra6.zephyr.codeanalysis.symbols.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

// TODO: change hashmap to a more efficient data structure

@RequiredArgsConstructor
public class ProgramInterpreter {
    private final Runtime runtime;
    private final BoundProgramScope program;
    private final HashMap<RuntimeType, HashMap<String, RuntimeType>> genericTypes = new HashMap<>();
    private final VariableTableStack variableTable = new VariableTableStack();

    private Object lastValue;

    public RuntimeType getRuntimeType(BoundProgramScope program, TypeSymbol type) {
        RuntimeType runtimeType = runtime.getRuntimeType(program, type);
        if (!runtimeType.isInitialized()) {
            initializeStaticFields(runtimeType);
        }
        return runtimeType;
    }

    private void initializeStaticFields(RuntimeType runtimeType) {
        for (FieldSymbol field : runtimeType.getSharedFields()) {
            BoundTypeScope scope = runtimeType.getScope();
            BoundExpression initializer = scope.getFieldInitializer(field);
            if (initializer != null) {
                Object value = evaluateExpression(initializer);
                runtimeType.assignSharedField(field, value);
            }
        }
        runtimeType.setInitialized(true);
    }

    private Object evaluateExpression(BoundExpression expression) {
        if (expression instanceof BoundLiteralExpression) {
            return evaluateLiteralExpression((BoundLiteralExpression) expression);
        }

        return switch (expression.getKind()) {
            case VARIABLE_EXPRESSION -> evaluateVariableExpression((BoundVariableExpression) expression);
            case INSTANCE_CREATION_EXPRESSION ->
                    evaluateInstanceCreationExpression((BoundInstanceCreationExpression) expression);
            case ASSIGNMENT_EXPRESSION -> evaluateAssignmentExpression((BoundAssignmentExpression) expression);
            case THIS_EXPRESSION -> evaluateThisExpression((BoundThisExpression) expression);
            case ARRAY_LITERAL_EXPRESSION -> evaluateArrayLiteralExpression((BoundArrayLiteralExpression) expression);
            case ARRAY_CREATION_EXPRESSION ->
                    evaluateArrayCreationExpression((BoundArrayCreationExpression) expression);
            case FUNCTION_CALL_EXPRESSION -> evaluateFunctionCallExpression((BoundFunctionCallExpression) expression);
            case BINARY_EXPRESSION -> evaluateBinaryExpression((BoundBinaryExpression) expression);
            case UNARY_EXPRESSION -> evaluateUnaryExpression((BoundUnaryExpression) expression);
            case MEMBER_ACCESS_EXPRESSION -> evaluateMemberAccessExpression((BoundMemberAccessExpression) expression);
            case INTERNAL_FUNCTION_EXPRESSION ->
                    evaluateInternalFunctionExpression((BoundInternalFunctionExpression) expression);
            case TYPE_EXPRESSION -> evaluateTypeExpression((BoundTypeExpression) expression);
            case ARRAY_ACCESS_EXPRESSION -> evaluateArrayAccessExpression((BoundArrayAccessExpression) expression);
            case CONVERSION_EXPRESSION -> evaluateConversionExpression((BoundConversionExpression) expression);
            case CONDITIONAL_EXPRESSION -> evaluateConditionalExpression((BoundConditionalExpression) expression);
            case FIELD_ACCESS_EXPRESSION -> evaluateFieldAccessExpression((BoundFieldAccessExpression) expression);
            case TYPE_CHECK_EXPRESSION -> evaluateTypeCheckExpression((BoundTypeCheckExpression) expression);
            default -> throw new RuntimeException("Unexpected expression: " + expression.getKind());
        };
    }

    private Object evaluateTypeCheckExpression(BoundTypeCheckExpression expression) {
        Object leftValue = evaluateExpression(expression.getLeftExpression());
        RuntimeType rightType = null;

        if (program.isTypeImported(expression.getRightType())) {
            ProgramInterpreter interpreter = runtime.findInterpreter(expression.getRightType());
            rightType = interpreter.getRuntimeType(program, expression.getRightType());
        }

        if (leftValue instanceof TypeInstance instance) {
            if (rightType == null) rightType = getRuntimeType(program, expression.getRightType());

            return instance.getRuntimeType().isAssignableTo(rightType);
        }

        if (leftValue instanceof RuntimeType type) {
            if (expression.getRightType().isGeneric()) {
                rightType = genericTypes.get(type).get(expression.getRightType().getName());
            }

            if (rightType == null) rightType = getRuntimeType(program, expression.getRightType());

            return type.isAssignableTo(rightType);
        }

        if (Types.isValidLiteralType(leftValue.getClass())) {
            TypeSymbol literalType = Types.getLiteralType(leftValue.getClass());

            if (expression.getRightType().isGeneric()) {
                TypeInstance thisType = variableTable.peek().keySet().stream()
                        .filter(symbol -> symbol.getName().equals("this"))
                        .findFirst()
                        .map(symbol -> (TypeInstance) variableTable.peek().get(symbol))
                        .orElse(null);

                if (thisType == null) {
                    throw new RuntimeException("Cannot find 'this' in type check expression");
                }

                rightType = genericTypes.get(thisType.getRuntimeType()).get(expression.getRightType().getName());
                return literalType.isAssignableTo(rightType.getType());
            } else {
                return literalType.isAssignableTo(expression.getRightType());
            }
        }

        throw new RuntimeException("Unexpected type for type check expression '%s'".formatted(leftValue.getClass().getSimpleName()));
    }

    private Object evaluateFieldAccessExpression(BoundFieldAccessExpression expression) {
        Object target = evaluateExpression(expression.getTarget());
        FieldSymbol field = expression.getField();

        if (target instanceof TypeInstance instance) {
            if (field.isShared()) {
                RuntimeType runtimeType = instance.getRuntimeType();
                return runtimeType.getSharedField(field);
            }
            return instance.getField(field);
        }

        if (target instanceof RuntimeType type) {
            if (!field.isShared()) {
                throw new RuntimeException("Cannot access instance field on type");
            }

            if (!type.getType().isFieldOrFunctionDeclared(field.getName())) {
                throw new RuntimeException("Type does not contain field " + field.getName());
            }

            if (!type.getType().isField(field.getName())) {
                throw new RuntimeException("Type does not contain field " + field.getName());
            }

            return type.getSharedField(field);
        }

        throw new RuntimeException("Unexpected target for field access: " + target);
    }


    private Object evaluateConditionalExpression(BoundConditionalExpression expression) {
        Object condition = evaluateExpression(expression.getCondition());
        if (!(condition instanceof Boolean conditionValue)) {
            throw new RuntimeException("Condition must be a boolean");
        }

        if (conditionValue) {
            return evaluateExpression(expression.getThenExpression());
        } else {
            return evaluateExpression(expression.getElseExpression());
        }
    }

    private Object evaluateConversionExpression(BoundConversionExpression expression) {
        // TODO: implement
        return evaluateExpression(expression.getExpression());
    }

    private Object evaluateArrayAccessExpression(BoundArrayAccessExpression expression) {
        Object array = evaluateExpression(expression.getTarget());
        Object index = evaluateExpression(expression.getIndex());

        if (!(array instanceof Object[] arrayValue)) {
            throw new RuntimeException("Cannot access array element of non-array");
        }

        if (!(index instanceof Integer indexValue)) {
            throw new RuntimeException("Cannot access array element with non-integer index");
        }

        if (indexValue < 0 || indexValue >= arrayValue.length) {
            throw new RuntimeException("Array index out of bounds");
        }

        return arrayValue[indexValue];
    }

    private Object evaluateTypeExpression(BoundTypeExpression expression) {
        TypeSymbol type = expression.getType();
        if (program.isTypeImported(type)) {
            // we use this because evaluator.getRuntimeType will return the runtime type of the imported type and also initialize missing shared fields
            ProgramInterpreter evaluator = runtime.findInterpreter(type);
            return evaluator.getRuntimeType(evaluator.program, type);
        }

        return getRuntimeType(program, type);
    }

    private Object evaluateInternalFunctionExpression(BoundInternalFunctionExpression expression) {
        IFunctionBase function = expression.getFunction();

        if (expression.getArguments().size() != function.getParameters().size())
            throw new RuntimeException("Invalid number of arguments for function %s".formatted(expression.getFunction()));

        HashMap<String, Object> arguments = new HashMap<>();

        if ((expression.getFunction() instanceof InternalFunction internalFunction && !internalFunction.isShared()) || expression.getFunction() instanceof InternalBinaryOperator || expression.getFunction() instanceof InternalUnaryOperator) {
            arguments.put("this", variableTable.peek().get(variableTable.peek().keySet().stream().filter(s -> s.getName().equals("this")).findFirst().orElseThrow()));
        }

        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            BoundExpression argument = expression.getArguments().get(i);

            Object value = evaluateExpression(argument);
            arguments.put(parameter.getName(), value);
        }

        return function.getFunctionBody().call(arguments);
    }

    private Object evaluateMemberAccessExpression(BoundMemberAccessExpression expression) {
        Object instanceValue = evaluateExpression(expression.getTarget());

        if (instanceValue instanceof RuntimeType type) {
            if (program.isTypeImported(type.getType())) {
                ProgramInterpreter evaluator = runtime.findInterpreter(type.getType());
                return evaluator.evaluateMemberAccessExpression(expression);
            }

            if (expression.getMember() instanceof FieldSymbol field) {
                return type.getSharedField(field);
            }

            throw new RuntimeException("Unexpected member access expression: " + expression.getMember().getKind());
        }

        if (expression.getTarget().getType() instanceof ArrayTypeSymbol) {
            // array fields
            if (expression.getMember() instanceof FieldSymbol field) {
                if (field.getName().equals("length")) {
                    return ((Object[]) instanceValue).length;
                }
            }
        }

        if (!(instanceValue instanceof TypeInstance instance)) {
            throw new RuntimeException("Cannot access member of non-instance");
        }

        if (expression.getMember() instanceof FieldSymbol field) {
            return instance.getField(field);
        }

        throw new RuntimeException("Unexpected member access expression: " + expression.getMember().getKind());
    }

    private Object evaluateUnaryExpression(BoundUnaryExpression expression) {
        RuntimeType operandType = runtime.findRuntimeType(expression.getOperand().getType());

        Object operandValue = evaluateExpression(expression.getOperand());

        String operator = expression.getOperator();

        BoundTypeScope scope = operandType.getScope();
        UnaryOperatorSymbol unaryOperator = operandType.getUnaryOperator(operator);

        if (unaryOperator == null)
            throw new RuntimeException("No unary operator " + operator + " found for type " + operandType.getName());

        variableTable.push(new VariableTable("unary operator (%s)".formatted(unaryOperator.getName())));
        assignLocalVariable(new VariableSymbol("this", true, operandType.getType()), operandValue);

        BoundBlockStatement body = scope.getUnaryOperatorBody(unaryOperator);
        Object result = evaluateStatement(body);
        variableTable.pop();

        return result;
    }

    private Object evaluateBinaryExpression(BoundBinaryExpression expression) {
        RuntimeType leftType = runtime.findRuntimeType(expression.getLeft().getType());
        RuntimeType rightType = runtime.findRuntimeType(expression.getRight().getType());

        Object thisValue = evaluateExpression(expression.getLeft());
        Object otherValue = evaluateExpression(expression.getRight());

        String operator = expression.getOperator();

        BoundTypeScope scope = leftType.getScope();
        BinaryOperatorSymbol binaryOperator = leftType.getBinaryOperator(operator, rightType);

        if (binaryOperator == null)
            throw new RuntimeException("No binary operator " + operator + " found for types " + leftType.getName() + " and " + rightType.getName());

        variableTable.push(new VariableTable("binary operator (%s)".formatted(binaryOperator.getName())));
        assignLocalVariable(new VariableSymbol("this", true, leftType.getType()), thisValue);
        assignLocalVariable(new VariableSymbol("other", true, rightType.getType()), otherValue);

        BoundBlockStatement body = scope.getBinaryOperatorBody(binaryOperator);
        Object result = evaluateStatement(body);
        variableTable.pop();

        return result;
    }

    private Object evaluateFunctionCallExpression(BoundFunctionCallExpression expression) {
        BoundExpression callee = expression.getCallee();

        // check if the callee is a type instance
        if (callee instanceof BoundVariableExpression variableExpression) {
            VariableSymbol variable = variableExpression.getVariable();

            if (Types.isBuiltinType(variable.getType())) {
                return evaluateBuiltinFunctionCall(callee, variable.getType(), expression.getFunction(), expression.getArguments(), null);
            }
        }

        Object calleeValue = evaluateExpression(callee);

        if (calleeValue instanceof TypeInstance instance) {
            return evaluateInstanceFunctionCall(instance, expression.getFunction(), expression.getArguments());
        }

        if (calleeValue instanceof RuntimeType type) {
            return evaluateTypeFunctionCall(type, expression.getFunction(), expression.getArguments());
        }

        if (calleeValue instanceof BoundLiteralExpression literal) {
            if (Types.isValidLiteralType(literal.getType())) {
                return evaluateBuiltinFunctionCall(callee, literal.getType(), expression.getFunction(), expression.getArguments(), calleeValue);
            }

            throw new RuntimeException("Cannot call function '%s' on literal of type '%s'".formatted(expression.getFunction().getName(), literal.getType().getName()));
        }

        if (Types.isValidLiteralType(calleeValue.getClass())) {
            return evaluateBuiltinFunctionCall(callee, Types.getLiteralType(calleeValue.getClass()), expression.getFunction(), expression.getArguments(), calleeValue);
        }

        if (calleeValue instanceof Object[]) {
            // Builtin array functions

            FunctionSymbol function = expression.getFunction();
            if (function.getName().equals("clone")) {
                return ((Object[]) calleeValue).clone();
            }

            throw new RuntimeException("Cannot call function '%s' on array".formatted(function.getName()));
        }

        throw new RuntimeException("Cannot call function '%s' on value of type '%s'".formatted(expression.getFunction().getName(), calleeValue.getClass().getName()));
    }

    private Object evaluateTypeFunctionCall(RuntimeType type, FunctionSymbol function, List<BoundExpression> arguments) {
        if (!function.isShared()) {
            throw new RuntimeException("Cannot call non-shared function '%s' on type '%s'".formatted(function.getName(), type.getName()));
        }

        if (program.isTypeImported(type.getType())) {
            ProgramInterpreter evaluator = runtime.findInterpreter(type.getType());

            List<Object> evaluatedArguments = new ArrayList<>();
            for (BoundExpression argument : arguments) {
                evaluatedArguments.add(evaluateExpression(argument));
            }

            return evaluator.evaluateTypeFunctionCallEvaluatedArgs(type, function, evaluatedArguments);
        }

        List<Object> evaluatedArguments = new ArrayList<>();
        for (BoundExpression argument : arguments) {
            evaluatedArguments.add(evaluateExpression(argument));
        }

        return evaluateTypeFunctionCallEvaluatedArgs(type, function, evaluatedArguments);
    }

    private Object evaluateTypeFunctionCallEvaluatedArgs(RuntimeType type, FunctionSymbol function, List<Object> evaluatedArguments) {
        variableTable.push(new VariableTable("type function (%s.%s)".formatted(type.getName(), function.getName())));
        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            Object value = evaluatedArguments.get(i);

            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            assignLocalVariable(variable, value);
        }

        BoundTypeScope scope = type.getScope();
        BoundBlockStatement body = scope.getFunctionBody(function);

        Object result = evaluateStatement(body);
        variableTable.pop();

        return result;
    }

    private Object evaluateBuiltinFunctionCall(BoundExpression callee, TypeSymbol type, FunctionSymbol function, List<BoundExpression> arguments, Object calleeValue) {
        List<Object> evaluatedArguments = new ArrayList<>();
        for (BoundExpression argument : arguments) {
            evaluatedArguments.add(evaluateExpression(argument));
        }

        if (calleeValue == null) calleeValue = evaluateExpression(callee);

        variableTable.push(new VariableTable("builtin function (%s.%s)".formatted(type.getName(), function.getName())));
        assignLocalVariable(new VariableSymbol("this", true, type), calleeValue);

        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            Object value = evaluatedArguments.get(i);

            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            assignLocalVariable(variable, value);
        }

        BoundTypeScope scope = runtime.findRuntimeType(type).getScope();
        BoundBlockStatement body = scope.getFunctionBody(function);
        Object result = evaluateStatement(body);
        variableTable.pop();

        return result;
    }

    private Object evaluateInstanceFunctionCall(TypeInstance instance, FunctionSymbol function, List<BoundExpression> arguments) {
        if (function.isShared()) {
            throw new RuntimeException("Cannot call shared function '%s' on instance of type '%s'".formatted(function.getName(), instance.getRuntimeType().getName()));
        }

        if (program.isTypeImported(instance.getRuntimeType().getType())) {
            ProgramInterpreter evaluator = runtime.findInterpreter(instance.getRuntimeType().getType());

            List<Object> evaluatedArguments = new ArrayList<>();
            for (BoundExpression argument : arguments) {
                evaluatedArguments.add(evaluateExpression(argument));
            }

            return evaluator.evaluateInstanceFunctionCallEvaluatedArgs(instance, function, evaluatedArguments);
        }

        List<Object> evaluatedArguments = new ArrayList<>();
        for (BoundExpression argument : arguments) {
            evaluatedArguments.add(evaluateExpression(argument));
        }

        return evaluateInstanceFunctionCallEvaluatedArgs(instance, function, evaluatedArguments);
    }

    private Object evaluateInstanceFunctionCallEvaluatedArgs(TypeInstance instance, FunctionSymbol function, List<Object> evaluatedArguments) {
        variableTable.push(new VariableTable("function (%s)".formatted(function.getName())));
        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            Object value = evaluatedArguments.get(i);

            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            assignLocalVariable(variable, value);
        }

        BoundTypeScope scope = instance.getRuntimeType().getScope();
        assignLocalVariable(new VariableSymbol("this", true, instance.getRuntimeType().getType()), instance);
        BoundBlockStatement body = scope.getFunctionBody(function);
        Object result = evaluateStatement(body);
        variableTable.pop();

        if (function.getType().equals(Types.VOID)) return null;
        return result;
    }

    private static Object[] createMultiDimensionalArray(List<Tuple<Integer, Object>> dimensions, int depth) {
        Tuple<Integer, Object> currentDimension = dimensions.get(0);

        Object[] result = new Object[currentDimension.getItem1()];
        if (depth == 1) {
            for (int i = 0; i < currentDimension.getItem1(); i++) {
                result[i] = currentDimension.getItem2();
            }
        } else {
            for (int i = 0; i < currentDimension.getItem1(); i++) {
                List<Tuple<Integer, Object>> subDimensions = dimensions.subList(1, dimensions.size());
                result[i] = createMultiDimensionalArray(subDimensions, depth - 1);
            }
        }
        return result;
    }

    private Object evaluateArrayCreationExpression(BoundArrayCreationExpression expression) {
        List<Tuple<Integer, Object>> dimensions = new ArrayList<>();

        for (BoundExpression dimension : expression.getDimensions().keySet()) {
            Object value = evaluateExpression(dimension);

            BoundExpression initializer = expression.getDimensions().get(dimension);
            Object initializerValue = initializer == null ? null : evaluateExpression(initializer);

            if (!(value instanceof Integer valueInt)) {
                throw new RuntimeException("Array dimension must be an integer");
            }

            if (valueInt < 0) {
                throw new RuntimeException("Array dimension must be positive");
            }

            dimensions.add(new Tuple<>(valueInt, initializerValue));
        }

        return createMultiDimensionalArray(dimensions, dimensions.size());
    }

    private Object evaluateArrayLiteralExpression(BoundArrayLiteralExpression expression) {
        List<Object> values = new ArrayList<>();

        for (BoundExpression element : expression.getElements()) {
            values.add(evaluateExpression(element));
        }

        return values.toArray();
    }

    private Object evaluateThisExpression(BoundThisExpression expression) {
        VariableSymbol variable = variableTable.peek().keySet().stream().filter(v -> v.getName().equals("this")).findFirst().orElseThrow(() -> new RuntimeException("No this variable found."));
        return variableTable.peek().get(variable);
    }

    private Object evaluateAssignmentExpression(BoundAssignmentExpression expression) {
        BoundExpression target = expression.getTarget();

        if (target instanceof BoundMemberAccessExpression memberAccess) {
            Object callee = evaluateExpression(memberAccess.getTarget());
            Symbol member = memberAccess.getMember();

            if (!(callee instanceof TypeInstance instance)) {
                if (!(callee instanceof RuntimeType type)) {
                    throw new RuntimeException("Cannot access member of non-instance");
                }

                if (!(member instanceof FieldSymbol field)) {
                    throw new RuntimeException("Cannot access member of non-instance");
                }

                Object value = evaluateExpression(expression.getExpression());

                if (program.isTypeImported(type.getType())) {
                    RuntimeType runtimeType = runtime.findRuntimeType(type.getType());
                    return runtimeType.assignSharedField(field, value);
                }

                return type.assignSharedField(field, value);
            }

            Object value = evaluateExpression(expression.getExpression());
            FieldSymbol field = instance.lookupField(member.getName());

            instance.setField(field, value);
            return value;
        }
        if (target instanceof BoundVariableExpression variableAccess) {
            Object value = evaluateExpression(expression.getExpression());
            assignLocalVariable(variableAccess.getVariable(), value);
            return value;
        }
        if (target instanceof BoundArrayAccessExpression arrayAccess) {
            Object array = evaluateExpression(arrayAccess.getTarget());
            Object index = evaluateExpression(arrayAccess.getIndex());
            Object value = evaluateExpression(expression.getExpression());

            if (!(array instanceof Object[] arrayObject)) {
                throw new RuntimeException("Cannot access element of non-array");
            }

            if (!(index instanceof Integer indexInt)) {
                throw new RuntimeException("Array index must be an integer");
            }

            if (indexInt < 0 || indexInt >= arrayObject.length) {
                throw new RuntimeException("Array index out of bounds");
            }

            arrayObject[indexInt] = value;
            return value;
        }

        throw new RuntimeException("Invalid assignment target: " + target.getKind());
    }

    private Object evaluateInstanceCreationExpression(BoundInstanceCreationExpression expression) {
        if (program.isTypeImported(expression.getType())) {
            ProgramInterpreter programInterpreter = runtime.findInterpreter(expression.getType());
            return programInterpreter.evaluateInstanceCreationExpression(expression);
        }

        RuntimeType runtimeType = getRuntimeType(program, expression.getType());
        BoundTypeScope scope = runtimeType.getScope();
        HashMap<FieldSymbol, Object> instanceFields = new HashMap<>();

        for (FieldSymbol field : expression.getType().getFields().stream().filter(f -> !f.isShared()).toList()) {
            Object value = null;
            if (scope.getFieldInitializer(field) != null) {
                value = evaluateExpression(scope.getFieldInitializer(field));
            }

            instanceFields.put(field, value);
        }

        HashMap<String, RuntimeType> genericTypes = new HashMap<>();
        TypeSymbol type = runtimeType.getType();

        if (runtimeType.hasGenerics()) {
            for (String key : expression.getGenericTypes().keySet()) {
                TypeSymbol genericType = expression.getGenericTypes().get(key);

                if (program.isTypeImported(genericType)) {
                    RuntimeType runtimeGenericType = runtime.findRuntimeType(genericType);
                    genericTypes.put(key, runtimeGenericType);
                } else {
                    genericTypes.put(key, getRuntimeType(program, genericType));
                }
            }
        }

        TypeInstance instance = runtimeType.createInstance(instanceFields, genericTypes);

        if (!this.genericTypes.containsKey(runtimeType)) {
            this.genericTypes.put(instance.getRuntimeType(), genericTypes);
        }

        ConstructorSymbol constructor = type.getConstructor(expression.getArguments().size());
        BoundBlockStatement body = scope.getConstructorBody(constructor);

        HashMap<VariableSymbol, Object> arguments = new HashMap<>();
        for (int i = 0; i < expression.getArguments().size(); i++) {
            BoundExpression argument = expression.getArguments().get(i);
            ParameterSymbol parameter = constructor.getParameters().get(i);

            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());

            Object value = evaluateExpression(argument);
            arguments.put(variable, value);
        }

        variableTable.push(new VariableTable("constructor (%s)".formatted(constructor.getName())));
        assignLocalVariable(new VariableSymbol("this", true, type), instance);

        for (VariableSymbol variable : arguments.keySet()) {
            assignLocalVariable(variable, arguments.get(variable));
        }

        evaluateStatement(body);
        variableTable.pop();
        return instance;
    }

    private Object evaluateVariableExpression(BoundVariableExpression expression) {
        VariableSymbol variable = expression.getVariable();

        if (variableTable.isEmpty()) {
            throw new RuntimeException("Variable table is empty.");
        }

        Optional<VariableSymbol> var = variableTable.peek().keySet().stream()
                .filter(v -> v.getName().equals(variable.getName()) &&
                        v.isReadonly() == variable.isReadonly() &&
                        v.getType().equals(variable.getType()))
                .findFirst();

        if (var.isEmpty()) {
            throw new RuntimeException("Variable not found: " + variable.getName());
        }
        return variableTable.peek().get(var.get());
    }

    private Object evaluateLiteralExpression(BoundLiteralExpression expression) {
        return expression.getValue();
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

    private void evaluateVariableDeclaration(BoundVariableDeclaration syntax) {
        VariableSymbol variable = syntax.getVariableSymbol();
        Object value = evaluateExpression(syntax.getInitializer());

        lastValue = value;

        assignLocalVariable(variable, value);
    }

    private void evaluateExpressionStatement(BoundExpressionStatement syntax) {
        lastValue = evaluateExpression(syntax.getExpression());
    }

    public Object evaluateFunctionWithEvaluatedArgs(RuntimeType type, FunctionSymbol function, Object[] arguments) {
        variableTable.push(new VariableTable("function (%s)".formatted(function.getName())));

        for (int i = 0; i < function.getParameters().size(); i++) {
            ParameterSymbol parameter = function.getParameters().get(i);
            VariableSymbol variable = new VariableSymbol(parameter.getName(), true, parameter.getType());
            assignLocalVariable(variable, arguments[i]);
        }

        BoundBlockStatement body = type.getFunctionBody(function);
        return evaluateStatement(body);
    }

    private void assignLocalVariable(VariableSymbol variable, Object value) {
        RuntimeLogger.tracef("Assigning %s to %s: %s %n", stringify(value), variable.getName(), variable.getType());
        variableTable.peek().put(variable, value);
    }

    private String stringify(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Object[]) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < ((Object[]) value).length; i++) {
                sb.append(stringify(((Object[]) value)[i]));
                if (i < ((Object[]) value).length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            return sb.toString();
        }
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        return value.toString();
    }
}
