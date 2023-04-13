package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class ContinueStatementSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken continueKeyword;
    @Getter
    private final SyntaxToken semicolonToken;

    public ContinueStatementSyntax(SyntaxTree syntaxTree, SyntaxToken continueKeyword, SyntaxToken semicolonToken) {
        super(syntaxTree);
        this.continueKeyword = continueKeyword;
        this.semicolonToken = semicolonToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.CONTINUE_STATEMENT;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(continueKeyword, semicolonToken);
    }
}
