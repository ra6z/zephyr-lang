package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TypeClauseSyntax extends SyntaxNode {
    @Getter
    private final SyntaxToken colonToken;
    @Getter
    private final QualifiedNameSyntax typeName;
    @Getter
    private final GenericParameterClauseSyntax genericParameterClause;


    public TypeClauseSyntax(SyntaxTree syntaxTree, SyntaxToken colonToken, QualifiedNameSyntax typeName, GenericParameterClauseSyntax genericParameterClause) {
        super(syntaxTree);

        this.colonToken = colonToken;
        this.typeName = typeName;
        this.genericParameterClause = genericParameterClause;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.TYPE_CLAUSE;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>(List.of(colonToken, typeName));
        if (genericParameterClause != null) result.add(genericParameterClause);
        return result;
    }
}
