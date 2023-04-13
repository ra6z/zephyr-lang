package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TypeFieldDeclarationSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken visibilityToken;
    @Getter
    private final SyntaxToken sharedToken;
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

    public TypeFieldDeclarationSyntax(SyntaxTree syntaxTree, SyntaxToken visibilityToken, SyntaxToken sharedToken, SyntaxToken keywordToken, SyntaxToken identifier, TypeClauseSyntax typeClause, SyntaxToken equalsToken, ExpressionSyntax initializer, SyntaxToken semicolonToken) {
        super(syntaxTree);
        this.visibilityToken = visibilityToken;
        this.sharedToken = sharedToken;
        this.keywordToken = keywordToken;
        this.identifier = identifier;
        this.typeClause = typeClause;
        this.equalsToken = equalsToken;
        this.initializer = initializer;
        this.semicolonToken = semicolonToken;
    }

    public boolean isShared() {
        return sharedToken != null;
    }

    public boolean isPublic() {
        return visibilityToken != null && visibilityToken.getKind() == SyntaxKind.PUB_KEYWORD;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.TYPE_FIELD_DECLARATION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> children = new ArrayList<>();
        if (visibilityToken != null) children.add(visibilityToken);
        if (sharedToken != null) children.add(sharedToken);
        children.add(keywordToken);
        children.add(identifier);
        children.add(typeClause);
        if (equalsToken != null) children.add(equalsToken);
        if (initializer != null) children.add(initializer);
        children.add(semicolonToken);
        return children;
    }
}
