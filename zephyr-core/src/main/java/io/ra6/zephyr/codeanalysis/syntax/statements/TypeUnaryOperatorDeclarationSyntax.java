package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import io.ra6.zephyr.codeanalysis.syntax.statements.BlockStatementSyntax;
import lombok.Getter;

import java.util.List;

public class TypeUnaryOperatorDeclarationSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken binaryOperatorKeyword;
    @Getter
    private final SyntaxToken operatorToken;
    @Getter
    private final SyntaxToken openParenToken;
    @Getter
    private final SyntaxToken closeParenToken;
    @Getter
    private final TypeClauseSyntax returnType;
    @Getter
    private final BlockStatementSyntax body;

    public TypeUnaryOperatorDeclarationSyntax(SyntaxTree syntaxTree, SyntaxToken binaryOperatorKeyword, SyntaxToken operatorToken, SyntaxToken openParenToken, SyntaxToken closeParenToken, TypeClauseSyntax returnType, BlockStatementSyntax body) {
        super(syntaxTree);
        this.binaryOperatorKeyword = binaryOperatorKeyword;
        this.operatorToken = operatorToken;
        this.openParenToken = openParenToken;
        this.closeParenToken = closeParenToken;
        this.returnType = returnType;
        this.body = body;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.TYPE_UNARY_OPERATOR_DECLARATION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(binaryOperatorKeyword, operatorToken, openParenToken, closeParenToken, returnType, body);
    }
}
