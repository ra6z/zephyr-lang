package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class ExpressionStatementSyntax extends StatementSyntax {
    @Getter
    private final ExpressionSyntax expression;
    @Getter
    private final SyntaxToken semicolonToken;

    public ExpressionStatementSyntax(SyntaxTree tree, ExpressionSyntax expression, SyntaxToken semicolonToken) {
        super(tree);
        this.expression = expression;
        this.semicolonToken = semicolonToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.EXPRESSION_STATEMENT;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(expression, semicolonToken);
    }
}
