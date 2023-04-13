package io.ra6.zephyr.codeanalysis.binding;

import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

public abstract class BoundNode {
    @Getter
    private final SyntaxNode syntax;

    protected BoundNode(SyntaxNode syntax) {
        this.syntax = syntax;
    }

    public abstract BoundNodeKind getKind();
}
