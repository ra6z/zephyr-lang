package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.List;

public class TypeClauseSyntax extends SyntaxNode {
    @Getter
    private final SyntaxToken colonToken;
    @Getter
    private final QualifiedNameSyntax typeName;
    @Getter
    private final SyntaxToken openBracketToken;
    @Getter
    private final SyntaxToken closeBracketToken;
    @Getter
    private final boolean isArray;


    public TypeClauseSyntax(SyntaxTree syntaxTree, SyntaxToken colonToken, QualifiedNameSyntax typeName, SyntaxToken openBracketToken, SyntaxToken closeBracketToken) {
        super(syntaxTree);

        this.colonToken = colonToken;
        this.typeName = typeName;
        this.openBracketToken = openBracketToken;
        this.closeBracketToken = closeBracketToken;
        this.isArray = this.openBracketToken != null && this.closeBracketToken != null;
    }

    public TypeClauseSyntax(SyntaxTree syntaxTree, SyntaxToken colonToken, QualifiedNameSyntax typeName) {
        this(syntaxTree, colonToken, typeName, null, null);
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.TYPE_CLAUSE;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        if (isArray)
            return List.of(colonToken, typeName, openBracketToken, closeBracketToken);
        return List.of(colonToken, typeName);
    }
}
