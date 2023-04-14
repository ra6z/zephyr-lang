package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.List;

public class GenericParameterSyntax extends SyntaxNode {
    @Getter
    private final SyntaxToken identifier;

    public GenericParameterSyntax(SyntaxTree tree, SyntaxToken identifier) {
        super(tree);
        this.identifier = identifier;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.GENERIC_PARAMETER;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(identifier);
    }
}
