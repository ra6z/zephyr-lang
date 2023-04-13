package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class DoWhileStatementSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken doKeyword;
    @Getter
    private final StatementSyntax body;
    @Getter
    private final SyntaxToken whileKeyword;
    @Getter
    private final SyntaxToken openParenToken;
    @Getter
    private final ExpressionSyntax condition;
    @Getter
    private final SyntaxToken closeParenToken;
    @Getter
    private final SyntaxToken semicolonToken;

    public DoWhileStatementSyntax(SyntaxTree syntaxTree, SyntaxToken doKeyword, StatementSyntax body, SyntaxToken whileKeyword, SyntaxToken openParenToken, ExpressionSyntax condition, SyntaxToken closeParenToken, SyntaxToken semicolonToken) {
        super(syntaxTree);
        this.doKeyword = doKeyword;
        this.body = body;
        this.whileKeyword = whileKeyword;
        this.openParenToken = openParenToken;
        this.condition = condition;
        this.closeParenToken = closeParenToken;
        this.semicolonToken = semicolonToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.DO_WHILE_STATEMENT;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(doKeyword, body, whileKeyword, openParenToken, condition, closeParenToken, semicolonToken);
    }
}
