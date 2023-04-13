package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ImportDeclarationSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken importKeyword;
    @Getter
    private final SyntaxToken stringToken;
    @Getter
    private final SyntaxToken asKeyword;
    @Getter
    private final SyntaxToken identifier;
    @Getter
    private final SyntaxToken semicolonToken;

    public ImportDeclarationSyntax(SyntaxTree syntaxTree, SyntaxToken importKeyword, SyntaxToken stringToken, SyntaxToken asKeyword, SyntaxToken identifier, SyntaxToken semicolonToken) {
        super(syntaxTree);
        this.importKeyword = importKeyword;
        this.stringToken = stringToken;
        this.asKeyword = asKeyword;
        this.identifier = identifier;
        this.semicolonToken = semicolonToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.IMPORT_DECLARATION;
    }

    public boolean hasName() {
        return asKeyword != null;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>();
        result.add(importKeyword);
        result.add(stringToken);
        if (asKeyword != null) {
            result.add(asKeyword);
            result.add(identifier);
        }
        result.add(semicolonToken);
        return result;
    }
}
