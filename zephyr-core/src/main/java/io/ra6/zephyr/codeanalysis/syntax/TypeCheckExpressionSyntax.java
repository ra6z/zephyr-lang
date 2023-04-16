package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TypeCheckExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final SyntaxToken isKeyword;
    @Getter
    private final ExpressionSyntax left;
    @Getter
    private final QualifiedNameSyntax qualifiedName;
    @Getter
    private final GenericParameterClauseSyntax genericParameterClause;

    public TypeCheckExpressionSyntax(SyntaxTree syntaxTree, ExpressionSyntax left, SyntaxToken isKeyword, QualifiedNameSyntax qualifiedName, GenericParameterClauseSyntax genericParameterClause) {
        super(syntaxTree);
        this.isKeyword = isKeyword;
        this.left = left;
        this.qualifiedName = qualifiedName;
        this.genericParameterClause = genericParameterClause;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.TYPE_CHECK_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>(List.of(left, isKeyword, qualifiedName));
        if (genericParameterClause != null) {
            result.add(genericParameterClause);
        }
        return result;
    }
}
