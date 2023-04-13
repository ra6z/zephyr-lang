package io.ra6.zephyr.codeanalysis.binding.statements;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundLabel;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.binding.BoundStatement;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import io.ra6.zephyr.codeanalysis.syntax.statements.WhileStatementSyntax;
import lombok.Getter;

public class BoundWhileStatement extends BoundStatement {
    @Getter
    private final BoundExpression condition;
    @Getter
    private final BoundStatement body;
    @Getter
    private final BoundLabel continueLabel;
    @Getter
    private final BoundLabel breakLabel;

    public BoundWhileStatement(SyntaxNode syntax, BoundExpression condition, BoundStatement body, BoundLabel continueLabel, BoundLabel breakLabel) {
        super(syntax);
        this.condition = condition;
        this.body = body;
        this.continueLabel = continueLabel;
        this.breakLabel = breakLabel;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.WHILE_STATEMENT;
    }
}
