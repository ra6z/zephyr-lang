package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class VariableDeclarationSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken keywordToken;
    @Getter
    private final SyntaxToken identifier;
    @Getter
    private final TypeClauseSyntax typeClause;
    @Getter
    private final SyntaxToken equalsToken;
    @Getter
    private final ExpressionSyntax initializer;
    @Getter
    private final SyntaxToken semicolonToken;

    public VariableDeclarationSyntax(SyntaxTree syntaxTree, SyntaxToken keywordToken, SyntaxToken identifier, TypeClauseSyntax typeClause, SyntaxToken equalsToken, ExpressionSyntax initializer, SyntaxToken semicolonToken) {
        super(syntaxTree);
        this.keywordToken = keywordToken;
        this.identifier = identifier;
        this.typeClause = typeClause;
        this.equalsToken = equalsToken;
        this.initializer = initializer;
        this.semicolonToken = semicolonToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.VARIABLE_DECLARATION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(keywordToken, identifier, typeClause, equalsToken, initializer, semicolonToken);
    }
}
