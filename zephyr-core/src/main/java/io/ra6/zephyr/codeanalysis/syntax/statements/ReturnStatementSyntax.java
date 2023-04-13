package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ReturnStatementSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken returnKeyword;
    @Getter
    private final ExpressionSyntax expression;
    @Getter
    private final SyntaxToken semicolonToken;

    public ReturnStatementSyntax(SyntaxTree syntaxTree, SyntaxToken returnKeyword, ExpressionSyntax expression, SyntaxToken semicolonToken) {
        super(syntaxTree);
        this.returnKeyword = returnKeyword;
        this.expression = expression;
        this.semicolonToken = semicolonToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.RETURN_STATEMENT;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>();
        result.add(returnKeyword);
        if (expression != null) result.add(expression);
        result.add(semicolonToken);
        return result;
    }
}
