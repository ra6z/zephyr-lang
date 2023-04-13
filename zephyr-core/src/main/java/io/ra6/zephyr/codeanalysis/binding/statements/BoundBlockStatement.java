package io.ra6.zephyr.codeanalysis.binding.statements;

import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.binding.BoundStatement;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

import java.util.List;

public class BoundBlockStatement extends BoundStatement {
    @Getter
    private final List<BoundStatement> statements;

    public BoundBlockStatement(SyntaxNode syntax, List<BoundStatement> statements) {
        super(syntax);
        this.statements = statements;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.BLOCK_STATEMENT;
    }
}
