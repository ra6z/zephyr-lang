package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class GenericParameterClauseSyntax extends SyntaxNode {
    @Getter
    private final SyntaxToken lessToken;
    @Getter
    private final SeparatedSyntaxList<GenericParameterSyntax> genericParameters;
    @Getter
    private final SyntaxToken greaterToken;

    public GenericParameterClauseSyntax(SyntaxTree tree, SyntaxToken lessToken, SeparatedSyntaxList<GenericParameterSyntax> genericParameters, SyntaxToken greaterToken) {
        super(tree);
        this.lessToken = lessToken;
        this.genericParameters = genericParameters;
        this.greaterToken = greaterToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.GENERIC_PARAMETER_CLAUSE;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>();
        result.add(lessToken);
        result.addAll(genericParameters.getNodesAndSeparators());
        result.add(greaterToken);
        return result;
    }
}
