package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class WhileStatementSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken whileKeyword;
    @Getter
    private final SyntaxToken openParenToken;
    @Getter
    private final ExpressionSyntax condition;
    @Getter
    private final SyntaxToken closeParenToken;
    @Getter
    private final StatementSyntax body;

    public WhileStatementSyntax(SyntaxTree syntaxTree, SyntaxToken whileKeyword, SyntaxToken openParenToken, ExpressionSyntax condition, SyntaxToken closeParenToken, StatementSyntax body) {
        super(syntaxTree);
        this.whileKeyword = whileKeyword;
        this.openParenToken = openParenToken;
        this.condition = condition;
        this.closeParenToken = closeParenToken;
        this.body = body;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.WHILE_STATEMENT;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(whileKeyword, openParenToken, condition, closeParenToken, body);
    }
}
