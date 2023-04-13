package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.List;

public class TypeClauseSyntax extends SyntaxNode {
    @Getter
    private final SyntaxToken colonToken;
    @Getter
    private final QualifiedNameSyntax typeName;


    public TypeClauseSyntax(SyntaxTree syntaxTree, SyntaxToken colonToken, QualifiedNameSyntax typeName) {
        super(syntaxTree);

        this.colonToken = colonToken;
        this.typeName = typeName;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.TYPE_CLAUSE;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(colonToken, typeName);
    }
}
