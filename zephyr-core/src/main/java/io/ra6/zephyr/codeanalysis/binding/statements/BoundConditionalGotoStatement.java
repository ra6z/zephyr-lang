package io.ra6.zephyr.codeanalysis.binding.statements;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundLabel;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.binding.BoundStatement;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

public class BoundConditionalGotoStatement extends BoundStatement {
    @Getter
    private final BoundLabel label;
    @Getter
    private final BoundExpression condition;
    private final boolean jumpIfTrue;

    public BoundConditionalGotoStatement(SyntaxNode syntax, BoundExpression condition, BoundLabel label, boolean jumpIfTrue) {
        super(syntax);
        this.label = label;
        this.condition = condition;
        this.jumpIfTrue = jumpIfTrue;
    }

    public boolean jumpIfTrue(){
        return jumpIfTrue;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.CONDITIONAL_GOTO_STATEMENT;
    }
}
