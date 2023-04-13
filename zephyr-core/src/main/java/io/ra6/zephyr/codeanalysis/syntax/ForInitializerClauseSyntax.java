package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.List;

public class ForInitializerClauseSyntax extends SyntaxNode {
    @Getter
    private final SyntaxToken varKeyword;
    @Getter
    private final TypeClauseSyntax typeClause;
    @Getter
    private final SyntaxToken identifierToken;
    @Getter
    private final SyntaxToken equalsToken;
    @Getter
    private final ExpressionSyntax initializer;

    public ForInitializerClauseSyntax(SyntaxTree syntaxTree, SyntaxToken varKeyword, TypeClauseSyntax typeClause, SyntaxToken identifierToken, SyntaxToken equalsToken, ExpressionSyntax initializer) {
        super(syntaxTree);
        this.varKeyword = varKeyword;
        this.typeClause = typeClause;
        this.identifierToken = identifierToken;
        this.equalsToken = equalsToken;
        this.initializer = initializer;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.FOR_INITIALIZER_CLAUSE;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(varKeyword, typeClause, identifierToken, equalsToken, initializer);
    }
}
