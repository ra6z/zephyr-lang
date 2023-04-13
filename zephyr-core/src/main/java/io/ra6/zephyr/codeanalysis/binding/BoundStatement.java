package io.ra6.zephyr.codeanalysis.binding;

import io.ra6.zephyr.codeanalysis.binding.BoundNode;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;

public abstract class BoundStatement extends BoundNode {
    protected BoundStatement(SyntaxNode syntax) {
        super(syntax);
    }
}
