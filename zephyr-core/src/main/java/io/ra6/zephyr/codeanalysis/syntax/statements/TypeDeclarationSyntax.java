package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TypeDeclarationSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken typeKeyword;
    @Getter
    private final SyntaxToken identifier;
    @Getter
    private final SyntaxToken openBraceToken;
    @Getter
    private final List<StatementSyntax> members;
    @Getter
    private final SyntaxToken closeBraceToken;

    public TypeDeclarationSyntax(SyntaxTree syntaxTree, SyntaxToken typeKeyword, SyntaxToken identifier, SyntaxToken openBraceToken, List<StatementSyntax> members, SyntaxToken closeBraceToken) {
        super(syntaxTree);
        this.typeKeyword = typeKeyword;
        this.identifier = identifier;
        this.openBraceToken = openBraceToken;
        this.members = members;
        this.closeBraceToken = closeBraceToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.TYPE_DECLARATION;
    }

    public boolean hasConstructor() {
        return members.stream().anyMatch(s -> s.getKind() == SyntaxKind.TYPE_CONSTRUCTOR_DECLARATION);
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>();
        result.add(typeKeyword);
        result.add(identifier);
        result.add(openBraceToken);
        result.addAll(members);
        result.add(closeBraceToken);
        return result;
    }
}
