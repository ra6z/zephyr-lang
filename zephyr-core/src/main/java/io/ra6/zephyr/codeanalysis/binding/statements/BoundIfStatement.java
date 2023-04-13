package io.ra6.zephyr.codeanalysis.binding.statements;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.binding.BoundStatement;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import io.ra6.zephyr.codeanalysis.syntax.statements.IfStatementSyntax;
import lombok.Getter;

public class BoundIfStatement extends BoundStatement {
    @Getter
    private final BoundExpression condition;
    @Getter
    private final BoundStatement thenStatement;
    @Getter
    private final BoundStatement elseStatement;

    public BoundIfStatement(SyntaxNode syntax, BoundExpression condition, BoundStatement thenStatement, BoundStatement elseStatement) {
        super(syntax);
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.IF_STATEMENT;
    }
}
