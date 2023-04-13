package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class BreakStatementSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken breakKeyword;
    @Getter
    private final SyntaxToken semicolonToken;

    public BreakStatementSyntax(SyntaxTree syntaxTree, SyntaxToken breakKeyword, SyntaxToken semicolonToken) {
        super(syntaxTree);
        this.breakKeyword = breakKeyword;
        this.semicolonToken = semicolonToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.BREAK_STATEMENT;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(breakKeyword, semicolonToken);
    }
}
