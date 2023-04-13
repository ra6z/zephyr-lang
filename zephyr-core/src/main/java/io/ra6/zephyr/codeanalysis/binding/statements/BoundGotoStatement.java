package io.ra6.zephyr.codeanalysis.binding.statements;

import io.ra6.zephyr.codeanalysis.binding.BoundLabel;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.binding.BoundStatement;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

public class BoundGotoStatement extends BoundStatement {
    @Getter
    private final BoundLabel label;

    public BoundGotoStatement(SyntaxNode syntax, BoundLabel label) {
        super(syntax);
        this.label = label;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.GOTO_STATEMENT;
    }
}
