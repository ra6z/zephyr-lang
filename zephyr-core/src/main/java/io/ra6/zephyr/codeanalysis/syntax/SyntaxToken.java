package io.ra6.zephyr.codeanalysis.syntax;

import io.ra6.zephyr.sourcefile.TextSpan;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class SyntaxToken extends SyntaxNode {
    @Getter
    private final SyntaxKind kind;
    @Getter
    private final TextSpan span;
    @Getter
    private final String text;
    @Getter
    private final Object value;
    @Getter
    private final boolean isMissing;

    public SyntaxToken(SyntaxTree tree, SyntaxKind kind, TextSpan span, String text, Object value) {
        super(tree);
        this.kind = kind;
        this.span = span;
        this.text = text == null ? "" : text;
        this.value = value;
        this.isMissing = text == null;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "SyntaxToken{" +
                "kind=" + kind +
                ", span=" + span +
                ", text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}
