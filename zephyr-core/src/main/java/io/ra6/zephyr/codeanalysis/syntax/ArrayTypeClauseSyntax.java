package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.List;

public class ArrayTypeClauseSyntax extends TypeClauseSyntax {
    @Getter
    private final List<SyntaxToken> brackets;

    public ArrayTypeClauseSyntax(SyntaxTree syntaxTree, SyntaxToken colonToken, QualifiedNameSyntax elementName, List<SyntaxToken> brackets) {
        super(syntaxTree, colonToken, elementName, null);
        this.brackets = brackets;
    }

    public int getRank() {
        return brackets.size() / 2;
    }
}
