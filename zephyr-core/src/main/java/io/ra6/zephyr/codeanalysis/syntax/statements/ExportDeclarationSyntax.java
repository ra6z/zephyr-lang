package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class ExportDeclarationSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken exportKeyword;
    @Getter
    private final QualifiedNameSyntax qualifiedName;
    @Getter
    private final SyntaxToken semicolonToken;

    public ExportDeclarationSyntax(SyntaxTree syntaxTree, SyntaxToken exportKeyword, QualifiedNameSyntax qualifiedName, SyntaxToken semicolonToken) {
        super(syntaxTree);
        this.exportKeyword = exportKeyword;
        this.qualifiedName = qualifiedName;
        this.semicolonToken = semicolonToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.EXPORT_DECLARATION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(exportKeyword, qualifiedName, semicolonToken);
    }
}
