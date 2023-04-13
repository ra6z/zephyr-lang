package io.ra6.zephyr.codeanalysis.binding.statements;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.binding.BoundStatement;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import io.ra6.zephyr.codeanalysis.syntax.statements.ReturnStatementSyntax;
import lombok.Getter;

public class BoundReturnStatement extends BoundStatement {
    @Getter
    private final BoundExpression expression;

    public BoundReturnStatement(SyntaxNode syntax, BoundExpression expression) {
        super(syntax);
        this.expression = expression;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.RETURN_STATEMENT;
    }
}
