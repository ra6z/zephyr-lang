package io.ra6.zephyr.codeanalysis.syntax;

import io.ra6.zephyr.Iterables;
import io.ra6.zephyr.sourcefile.TextSpan;
import io.ra6.zephyr.sourcefile.TextLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.ExtensionMethod;

import java.util.List;

@AllArgsConstructor
@ExtensionMethod(Iterables.class)
public abstract class SyntaxNode {
    @Getter
    private final SyntaxTree tree;

    public abstract SyntaxKind getKind();

    public TextSpan getSpan() {
        TextSpan first = Iterables.first(getChildren()).getSpan();
        TextSpan last = Iterables.last(getChildren()).getSpan();
        return TextSpan.fromBounds(first.getStart(), last.getEnd());
    }

    public TextLocation getLocation() {
        return new TextLocation(tree.getSourceText(), getSpan());
    }

    public abstract List<SyntaxNode> getChildren();

    public SyntaxToken getLastToken() {
        if (this instanceof SyntaxToken token)
            return token;
        return getChildren().last().getLastToken();
    }
}
