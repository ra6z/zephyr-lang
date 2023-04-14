package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ArrayCreationExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final SyntaxToken newKeyword;
    @Getter
    private final QualifiedNameSyntax qualifiedName;
    @Getter
    private final GenericParameterClauseSyntax genericParameter;
    @Getter
    private final List<ArraySizeClauseSyntax> arraySizeClauses;

    public ArrayCreationExpressionSyntax(SyntaxTree syntaxTree, SyntaxToken newKeyword, QualifiedNameSyntax qualifiedName, GenericParameterClauseSyntax genericParameter, List<ArraySizeClauseSyntax> arraySizeClauses) {
        super(syntaxTree);
        this.newKeyword = newKeyword;
        this.qualifiedName = qualifiedName;
        this.genericParameter = genericParameter;
        this.arraySizeClauses = arraySizeClauses;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.ARRAY_CREATION_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        ArrayList<SyntaxNode> result = new ArrayList<>();
        result.add(newKeyword);
        result.add(qualifiedName);
        if (genericParameter != null) result.add(genericParameter);
        result.addAll(arraySizeClauses);
        return result;
    }
}
