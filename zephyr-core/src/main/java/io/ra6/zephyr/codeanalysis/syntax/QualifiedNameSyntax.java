package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class QualifiedNameSyntax extends SyntaxNode {
    @Getter
    private final SeparatedSyntaxList<SyntaxToken> identifiers;

    public QualifiedNameSyntax(SyntaxTree tree, SeparatedSyntaxList<SyntaxToken> identifiers) {
        super(tree);
        this.identifiers = identifiers;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.QUALIFIED_NAME;
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (SyntaxNode token : identifiers.getNodesAndSeparators()) {
            if (token instanceof SyntaxToken tk) {
                sb.append(tk.getText());
            }
        }
        return sb.toString();
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return new ArrayList<>(identifiers.getNodesAndSeparators());
    }
}
