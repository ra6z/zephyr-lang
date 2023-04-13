package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.List;

public class ElseClauseSyntax extends SyntaxNode {
    @Getter
    private final SyntaxToken elseKeyword;
    @Getter
    private final StatementSyntax elseStatement;

    public ElseClauseSyntax(SyntaxTree syntaxTree, SyntaxToken elseKeyword, StatementSyntax elseStatement) {
        super(syntaxTree);
        this.elseKeyword = elseKeyword;
        this.elseStatement = elseStatement;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.ELSE_CLAUSE;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(elseKeyword, elseStatement);
    }
}
