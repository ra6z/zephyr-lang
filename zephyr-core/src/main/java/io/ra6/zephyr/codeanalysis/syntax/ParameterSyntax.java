package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.List;

public class ParameterSyntax extends SyntaxNode {
    @Getter
    private final SyntaxToken identifier;
    @Getter
    private final TypeClauseSyntax typeClause;

    public ParameterSyntax(SyntaxTree syntaxTree, SyntaxToken identifier, TypeClauseSyntax typeClause) {
        super(syntaxTree);
        this.identifier = identifier;
        this.typeClause = typeClause;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.PARAMETER;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(identifier, typeClause);
    }
}
