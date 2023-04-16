package io.ra6.zephyr.diagnostic;

import io.ra6.zephyr.codeanalysis.symbols.ConstructorSymbol;
import io.ra6.zephyr.codeanalysis.symbols.FunctionSymbol;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxFacts;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxKind;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import io.ra6.zephyr.sourcefile.TextLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class DiagnosticBag {
    private final List<Diagnostic> diagnostics = new ArrayList<>();

    public void addAll(Collection<Diagnostic> other) {
        diagnostics.addAll(other);
    }

    public void addAll(DiagnosticBag diagnostics) {
        addAll(diagnostics.diagnostics);
    }

    public ArrayList<Diagnostic> asList() {
        return new ArrayList<>(diagnostics);
    }

    private void report(Diagnostic diagnostic) {
        diagnostics.add(diagnostic);
    }

    private void reportError(TextLocation location, String message) {
        report(Diagnostic.error(location, message));
    }

    private void reportError(TextLocation location, String message, String hint) {
        report(Diagnostic.error(location, message, hint));
    }

    private void reportWarning(TextLocation location, String message) {
        report(Diagnostic.warning(location, message));
    }

    private void reportWarning(TextLocation location, String message, String hint) {
        report(Diagnostic.warning(location, message, hint));
    }

    public void reportBadCharacter(TextLocation location, char current) {
        String message = "Encountered invalid character: '%c'".formatted(current);
        reportError(location, message);
    }

    public void reportUnexpectedToken(TextLocation location, SyntaxKind actual, SyntaxKind expected) {
        String message = "Encountered unexpected token <%s>. Expected <%s>.".formatted(actual, expected);
        reportError(location, message);
    }

    public void reportUnterminatedString(TextLocation location) {
        String message = "Unterminated string literal.";
        String hint = "String literals must be terminated with a double quote (\").";
        reportError(location, message, hint);
    }

    public void reportInvalidNumber(TextLocation location, String text) {
        String message = "The number %s is not a valid number.".formatted(text);
        String hint = "Numbers must be a sequence of digits with an optional fractional part and an optional exponent part.";
        reportError(location, message, hint);
    }

    public void reportUnexpectedTypeMember(TextLocation location, SyntaxKind kind, SyntaxKind[] syntaxKinds) {
        String message = "Encountered unexpected type member <%s>. Expected one of <%s>.".formatted(kind, Arrays.toString(Arrays.stream(syntaxKinds).map(SyntaxFacts::getText).toArray()));
        reportError(location, message);
    }

    public void reportUnexpectedCompilationUnitMember(TextLocation location, SyntaxKind kind, SyntaxKind[] syntaxKinds) {
        String message = "Encountered unexpected compilation unit member <%s>. Expected one of <%s>.".formatted(kind, Arrays.toString(Arrays.stream(syntaxKinds).map(SyntaxFacts::getText).toArray()));
        reportError(location, message);
    }

    public void reportTypeAlreadyDeclared(TextLocation location, String typeName) {
        String message = "Type '%s' is already declared.".formatted(typeName);
        String hint = "Types must have unique names.";
        reportError(location, message, hint);
    }

    public void reportConstFieldMustHaveInitializer(TextLocation location, String fieldName) {
        String message = "Const field '%s' must have an initializer.".formatted(fieldName);
        String hint = "Const fields must be initialized with a value.";
        reportError(location, message, hint);
    }

    public void reportUnknownType(TextLocation location, String text) {
        String message = "Unknown type '%s'.".formatted(text);
        String hint = "Make sure the type is declared in the current scope.";
        reportError(location, message, hint);
    }

    public void reportParameterAlreadyDeclared(TextLocation location, String parameterName) {
        String message = "Parameter '%s' is already declared.".formatted(parameterName);
        String hint = "Parameters must have unique names.";
        reportError(location, message, hint);
    }

    public void reportConstructorParameterWithCountAlreadyDefined(TextLocation textLocation, int size) {
        String message = "Constructor with %d parameters is already defined.".formatted(size);
        reportError(textLocation, message);
    }

    public void reportBinaryOperatorAlreadyDeclared(TextLocation location, String operatorName, TypeSymbol left, TypeSymbol right) {
        String message = "Binary operator is already declared for '%s' '%s' '%s'.".formatted(left, operatorName, right);
        reportError(location, message);
    }

    public void reportUnaryOperatorAlreadyDeclared(TextLocation location, String operatorName) {
        String message = "Unary operator is already declared for '%s'.".formatted(operatorName);
        reportError(location, message);
    }

    public void reportImportError(TextLocation location, String message) {
        String hint = "Make sure the file exists and is in the correct directory.";
        reportError(location, message, hint);
    }

    public void reportTodoFeature(TextLocation location, String feature) {
        String message = "Feature '%s' is not yet implemented.".formatted(feature);
        reportWarning(location, message);
    }

    public void reportInvalidNumberLiteral(TextLocation location, String text) {
        String message = "The number '%s' is not a valid number.".formatted(text);
        String hint = "Numbers must be a sequence of digits with an optional fractional part and an optional exponent part.";
        reportError(location, message, hint);
    }

    public void reportUndefinedBinaryOperator(TextLocation location, String text, TypeSymbol leftType, TypeSymbol rightType) {
        String message = "Binary operator '%s' is not defined for types '%s' and '%s'.".formatted(text, leftType, rightType);
        String hint = "Make sure the operator is defined for the given types.";
        reportError(location, message, hint);
    }

    public void reportUndefinedUnaryOperator(TextLocation location, String text, TypeSymbol operandType) {
        String message = "Unary operator '%s' is not defined for type '%s'.".formatted(text, operandType);
        String hint = "Make sure the operator is defined for the given type.";
        reportError(location, message, hint);
    }

    public void reportInvalidConditionType(TextLocation location, TypeSymbol type) {
        String message = "The condition must be of type 'bool', but was of type '%s'.".formatted(type);
        String hint = "Make sure the condition is of type 'bool'.";
        reportError(location, message, hint);
    }

    public void reportMismatchingTypes(TextLocation location, TypeSymbol expected, TypeSymbol actual) {
        String message = "Expected type '%s', but was '%s'.".formatted(expected, actual);
        String hint = "Make sure the types match.";
        reportError(location, message, hint);
    }

    public void reportConstructorParameterWithCountNotDefined(TextLocation location, TypeSymbol type, int size) {
        String message = "Constructor with %d parameters is not defined for type '%s'.".formatted(size, type);
        String hint = "Make sure the constructor is defined.";
        reportError(location, message, hint);
    }

    public void reportUndefinedName(TextLocation location, String name) {
        String message = "Undefined name '%s'.".formatted(name);
        String hint = "Make sure the name is declared in the current scope.";
        reportError(location, message, hint);
    }

    public void reportCannotAssign(TextLocation location, String name) {
        String message = "Variable '%s' is read-only and cannot be assigned to.".formatted(name);
        String hint = "Make sure the variable is not declared as 'const' or 'readonly'.";
        reportError(location, message, hint);
    }

    public void reportInvalidAssignmentTarget(TextLocation location, SyntaxNode syntax) {
        String message = "Expression of type '%s' cannot be used as an assignment target.".formatted(syntax.getClass().getName());
        String hint = "Make sure the expression is a variable, a field access or an array element access.";
        reportError(location, message, hint);
    }

    public void reportCannotConvert(TextLocation location, TypeSymbol actualType, TypeSymbol targetType) {
        String message = "Cannot convert type '%s' to '%s'.".formatted(actualType, targetType);
        String hint = "Make sure the types are compatible.";
        reportError(location, message, hint);
    }

    public void reportInvalidBreakOrContinue(TextLocation location, String name) {
        String message = "'%s' is not allowed here.".formatted(name);
        String hint = "Make sure the '%s' is inside a loop.".formatted(name);
        reportError(location, message, hint);
    }

    public void reportInvalidReturn(TextLocation location) {
        String message = "'return' is not allowed here.";
        String hint = "Make sure the 'return' is inside a function.";
        reportError(location, message, hint);
    }

    public void cannotReturnExpressionOnVoidFunction(TextLocation location) {
        String message = "Cannot return an expression from a void function.";
        String hint = "Make sure the function is not declared as 'void'.";
        reportError(location, message, hint);
    }

    public void cannotReturnVoidOnNonVoidFunction(TextLocation location) {
        String message = "Cannot return void from a non-void function.";
        String hint = "Make sure the function is declared as 'void'.";
        reportError(location, message, hint);
    }

    public void reportVariableAlreadyDeclared(TextLocation location, String variableName) {
        String message = "Variable '%s' is already declared in this scope.".formatted(variableName);
        String hint = "Make sure each variable is declared only once.";
        reportError(location, message, hint);
    }

    public void reportConstVariableMustBeInitialized(TextLocation location, String variableName) {
        String message = "Const variable '%s' must be initialized.".formatted(variableName);
        reportError(location, message);
    }

    public void reportUndefinedType(TextLocation location, String name) {
        String message = "Undefined type '%s'.".formatted(name);
        String hint = "Make sure the type is declared in the current scope.";
        reportError(location, message, hint);
    }

    public void reportExportAlreadyDeclared(TextLocation location, String name) {
        String message = "Export '%s' is already declared.".formatted(name);
        String hint = "Make sure each export is declared only once.";
        reportError(location, message, hint);
    }

    public boolean hasErrors() {
        return diagnostics.stream().anyMatch(Diagnostic::isError);
    }

    public boolean hasWarnings() {
        return diagnostics.stream().anyMatch(Diagnostic::isWarning);
    }

    public void reportCannotCallThis(TextLocation location) {
        String message = "'this' cannot be called.";
        String hint = "Make sure the 'this' is not called.";
        reportError(location, message, hint);
    }

    public void reportFieldNotDeclared(TextLocation location, String fieldName) {
        String message = "Field '%s' is not declared.".formatted(fieldName);
        String hint = "Make sure the field is declared in the current scope.";
        reportError(location, message, hint);
    }

    public void reportFunctionNotDeclared(TextLocation location, String functionName) {
        String message = "Function '%s' is not declared.".formatted(functionName);
        String hint = "Make sure the function is declared in the current scope.";
        reportError(location, message, hint);
    }

    public void reportBinaryOperatorNotDeclared(TextLocation location, String operatorName, TypeSymbol type) {
        String message = "Binary operator '%s' is not declared for type '%s'.".formatted(operatorName, type);
        String hint = "Make sure the operator is declared in the type.";
        reportError(location, message, hint);
    }

    public void reportUnaryOperatorNotDeclared(TextLocation location, String operatorName, TypeSymbol type) {
        String message = "Unary operator '%s' is not declared for type '%s'.".formatted(operatorName, type);
        String hint = "Make sure the operator is declared in the type.";
        reportError(location, message, hint);
    }

    public void reportFieldAlreadyDeclared(TextLocation location, String fieldName) {
        String message = "Field '%s' is already declared in this scope.".formatted(fieldName);
        String hint = "Make sure each field is declared only once.";
        reportError(location, message, hint);
    }

    public void reportFunctionDeclaredButFieldExpected(TextLocation location, String fieldName) {
        String message = "Expected a field but found a function named '%s'.".formatted(fieldName);
        String hint = "Make sure each field is declared only once.";
        reportError(location, message, hint);
    }

    public void reportFunctionAlreadyDeclared(TextLocation location, String functionName) {
        String message = "Function '%s' is already declared in this scope.".formatted(functionName);
        String hint = "Make sure each function is declared only once.";
        reportError(location, message, hint);
    }

    public void reportFieldDeclaredButFunctionExpected(TextLocation location, String functionName) {
        String message = "Expected a function but found a field named '%s'.".formatted(functionName);
        String hint = "Make sure each function is declared only once.";
        reportError(location, message, hint);
    }

    public void reportInvalidMemberAccess(TextLocation location, SyntaxNode syntax) {
        String message = "Expression of type '%s' cannot be used as a member access target.".formatted(syntax.getClass().getName());
        String hint = "Make sure the expression is a variable or a field access.";
        reportError(location, message, hint);
    }

    public void reportInvalidReturnExpression(TextLocation location, TypeSymbol actualType, TypeSymbol expectedType) {
        String message = "Cannot return an expression of type '%s' from a function of type '%s'.".formatted(actualType, expectedType);
        String hint = "Make sure the expression matches the function type.";
        reportError(location, message, hint);
    }

    public void reportUndefinedMember(TextLocation location, String name, String member) {
        String message = "Type '%s' does not contain a member named '%s'.".formatted(name, member);
        String hint = "Make sure the member is declared in the type.";
        reportError(location, message, hint);
    }

    public void cannotReturnExpressionOnConstructor(TextLocation location) {
        String message = "Cannot return an expression from a constructor.";
        String hint = "Make sure the constructor is not declared as 'void'.";
        reportError(location, message, hint);
    }

    public void reportUnaryOperatorCannotBeVoid(TextLocation location, String operatorName) {
        String message = "Unary operator '%s' cannot be declared as 'void'.".formatted(operatorName);
        String hint = "Make sure the operator is not declared as 'void'.";
        reportError(location, message, hint);
    }

    public void reportCannotAccessPrivateMember(TextLocation location, String name, String member, String memberType) {
        String message = "Cannot access private %s '%s' of type '%s'.".formatted(member, memberType, name);
        String hint = "Make sure the member is declared as 'public'.";
        reportError(location, message, hint);
    }

    public void reportCannotIndex(TextLocation location, TypeSymbol type) {
        String message = "Cannot index type '%s'.".formatted(type);
        String hint = "Make sure the type is indexable.";
        reportError(location, message, hint);
    }

    public void reportArrayCreationMustHaveSize(TextLocation location) {
        String message = "Array creation must have a size.";
        String hint = "Make sure the array creation has a size.";
        reportError(location, message, hint);
    }

    public void reportArrayCreationSizeMustBeInt(TextLocation location) {
        String message = "Array creation size must be of type 'int'.";
        String hint = "Make sure the array creation size is of type 'int'.";
        reportError(location, message, hint);
    }

    public void reportGenericAlreadyDeclared(TextLocation location, String genericName) {
        String message = "Generic '%s' is already declared in this scope.".formatted(genericName);
        String hint = "Make sure each generic is declared only once.";
        reportError(location, message, hint);
    }

    public void reportGenericParameterCountMismatch(TextLocation textLocation, TypeSymbol type, int genericCount) {
        String message = "Generic count mismatch for type '%s'. Expected %d but found %d.".formatted(type, type.getGenericCount(), genericCount);
        String hint = "Make sure the generic count matches the type.";
        reportError(textLocation, message, hint);
    }

    public void reportFieldNameIsReserved(TextLocation location, String fieldName) {
        String message = "Field name '%s' is reserved.".formatted(fieldName);
        String hint = "Make sure the field name is not reserved.";
        reportError(location, message, hint);
    }

    public void reportInvalidToStringFunction(TextLocation location, String typeName, String cause) {
        String message = "Invalid 'toString' function for type '%s'. %s".formatted(typeName, cause);
        String hint = "Make sure the 'toString' function is valid.";
        reportError(location, message, hint);
    }

    public void reportReservedTypeName(TextLocation location, String name) {
        String message = "Type name '%s' is reserved.".formatted(name);
        String hint = "Make sure the type name is not reserved.";
        reportError(location, message, hint);
    }

    public void reportInvalidCharacterLiteral(TextLocation location) {
        String message = "Invalid character literal.";
        String hint = "Make sure the character literal is valid.";
        reportError(location, message, hint);
    }

    public void reportInvalidEscapeSequence(TextLocation location) {
        String message = "Invalid escape sequence.";
        String hint = "Make sure the escape sequence is valid.";
        reportError(location, message, hint);
    }

    public void reportUnterminatedChar(TextLocation location) {
        String message = "Unterminated character literal.";
        String hint = "Make sure the character literal is terminated.";
        reportError(location, message, hint);
    }

    public void reportInvalidUnicodeEscapeSequence(TextLocation location) {
        String message = "Invalid unicode escape sequence.";
        String hint = "Make sure the unicode escape sequence is valid.";
        reportError(location, message, hint);
    }

    public void reportArrayCreationInitializerTypeMismatch(TextLocation location, TypeSymbol expected, TypeSymbol actual) {
        String message = "Array creation initializer type mismatch. Expected '%s' but found '%s'.".formatted(expected, actual);
        String hint = "Make sure the array creation initializer type matches the array type.";
        reportError(location, message, hint);
    }

    public void reportFunctionParameterCountMismatch(TextLocation location, FunctionSymbol function, int size) {
        String message = "Function parameter count mismatch. Expected %d but found %d.".formatted(function.getParameters().size(), size);
        String hint = "Make sure the function parameter count matches the call.";
        reportError(location, message, hint);
    }

    public void reportConstructorParameterCountMismatch(TextLocation location, ConstructorSymbol constructor, int size) {
        String message = "Constructor parameter count mismatch. Expected %d but found %d.".formatted(constructor.getParameters().size(), size);
        String hint = "Make sure the constructor parameter count matches the call.";
        reportError(location, message, hint);
    }

    public void reportCannotConvertArrayLiteral(TextLocation location, TypeSymbol type, TypeSymbol type1) {
        String message = "Cannot convert array literal of type '%s' to type '%s'.".formatted(type, type1);
        String hint = "Make sure the array literal can be converted to the target type.";
        reportError(location, message, hint);
    }
}
