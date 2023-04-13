package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class IfStatementSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken ifKeyword;
    @Getter
    private final SyntaxToken openParenToken;
    @Getter
    private final ExpressionSyntax condition;
    @Getter
    private final SyntaxToken closeParenToken;
    @Getter
    private final StatementSyntax thenStatement;
    @Getter
    private final ElseClauseSyntax elseClause;

    public IfStatementSyntax(SyntaxTree syntaxTree, SyntaxToken ifKeyword, SyntaxToken openParenToken, ExpressionSyntax condition, SyntaxToken closeParenToken, StatementSyntax thenStatement, ElseClauseSyntax elseClause) {
        super(syntaxTree);
        this.ifKeyword = ifKeyword;
        this.openParenToken = openParenToken;
        this.condition = condition;
        this.closeParenToken = closeParenToken;
        this.thenStatement = thenStatement;
        this.elseClause = elseClause;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.IF_STATEMENT;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>();
        result.add(ifKeyword);
        result.add(openParenToken);
        result.add(condition);
        result.add(closeParenToken);
        result.add(thenStatement);
        if (elseClause != null) result.add(elseClause);
        return result;
    }
}
