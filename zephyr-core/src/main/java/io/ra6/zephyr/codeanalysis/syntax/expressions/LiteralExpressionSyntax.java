package io.ra6.zephyr.codeanalysis.syntax.expressions;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.List;

public class LiteralExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final SyntaxToken valueToken;

    public LiteralExpressionSyntax(SyntaxTree syntaxTree, SyntaxToken valueToken) {
        super(syntaxTree);
        this.valueToken = valueToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.LITERAL_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(valueToken);
    }
}
