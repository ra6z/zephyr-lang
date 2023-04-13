package io.ra6.zephyr.codeanalysis.binding.statements;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.binding.BoundStatement;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

public class BoundExpressionStatement extends BoundStatement {
    @Getter
    private final BoundExpression expression;

    public BoundExpressionStatement(SyntaxNode syntax, BoundExpression expression) {
        super(syntax);
        this.expression = expression;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.EXPRESSION_STATEMENT;
    }
}
