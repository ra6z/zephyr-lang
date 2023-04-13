package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ForStatementSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken forKeyword;
    @Getter
    private final SyntaxToken openParenToken;
    @Getter
    private final ForInitializerClauseSyntax initializer;
    @Getter
    private final SyntaxToken initializerSemicolonToken;
    @Getter
    private final ExpressionSyntax condition;
    @Getter
    private final SyntaxToken conditionSemicolonToken;
    @Getter
    private final ExpressionSyntax incrementer;
    @Getter
    private final SyntaxToken closeParenToken;
    @Getter
    private final StatementSyntax body;

    public ForStatementSyntax(SyntaxTree syntaxTree, SyntaxToken forKeyword, SyntaxToken openParenToken, ForInitializerClauseSyntax initializer, SyntaxToken initializerSemicolonToken, ExpressionSyntax condition, SyntaxToken conditionSemicolonToken, ExpressionSyntax incrementer, SyntaxToken closeParenToken, StatementSyntax body) {
        super(syntaxTree);
        this.forKeyword = forKeyword;
        this.openParenToken = openParenToken;
        this.initializer = initializer;
        this.initializerSemicolonToken = initializerSemicolonToken;
        this.condition = condition;
        this.conditionSemicolonToken = conditionSemicolonToken;
        this.incrementer = incrementer;
        this.closeParenToken = closeParenToken;
        this.body = body;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.FOR_STATEMENT;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>();
        result.add(forKeyword);
        result.add(openParenToken);
        if (initializer != null) result.add(initializer);
        result.add(initializerSemicolonToken);
        if (condition != null) result.add(condition);
        result.add(conditionSemicolonToken);
        if (incrementer != null) result.add(incrementer);
        result.add(closeParenToken);
        result.add(body);
        return result;
    }
}
