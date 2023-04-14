package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.List;

public class NativeTypeDeclarationSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken nativeTypeKeyword;
    @Getter
    private final SyntaxToken identifier;
    @Getter
    private final SyntaxToken semicolonToken;

    public NativeTypeDeclarationSyntax(SyntaxTree syntaxTree, SyntaxToken nativeTypeKeyword, SyntaxToken identifier, SyntaxToken semicolonToken) {
        super(syntaxTree);
        this.nativeTypeKeyword = nativeTypeKeyword;
        this.identifier = identifier;
        this.semicolonToken = semicolonToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.NATIVE_TYPE_DECLARATION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(nativeTypeKeyword, identifier, semicolonToken);
    }
}
