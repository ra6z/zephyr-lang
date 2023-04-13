package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BlockStatementSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken openBraceToken;
    @Getter
    private final List<StatementSyntax> statements;
    @Getter
    private final SyntaxToken closeBraceToken;

    public BlockStatementSyntax(SyntaxTree syntaxTree, SyntaxToken openBraceToken, List<StatementSyntax> statements, SyntaxToken closeBraceToken) {
        super(syntaxTree);
        this.openBraceToken = openBraceToken;
        this.statements = statements;
        this.closeBraceToken = closeBraceToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.BLOCK_STATEMENT;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>();
        result.add(openBraceToken);
        result.addAll(statements);
        result.add(closeBraceToken);
        return result;
    }
}
