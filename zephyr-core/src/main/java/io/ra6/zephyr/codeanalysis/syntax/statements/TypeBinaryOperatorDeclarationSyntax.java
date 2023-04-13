package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import io.ra6.zephyr.codeanalysis.syntax.statements.BlockStatementSyntax;
import lombok.Getter;

import java.util.List;

public class TypeBinaryOperatorDeclarationSyntax extends StatementSyntax {

    @Getter
    private final SyntaxToken binaryOperatorKeyword;
    @Getter
    private final SyntaxToken operatorToken;
    @Getter
    private final SyntaxToken openParenToken;
    @Getter
    private final SyntaxToken rightOperandToken;
    @Getter
    private final TypeClauseSyntax rightOperandType;
    @Getter
    private final SyntaxToken closeParenToken;
    @Getter
    private final TypeClauseSyntax returnType;
    @Getter
    private final BlockStatementSyntax body;

    public TypeBinaryOperatorDeclarationSyntax(SyntaxTree syntaxTree, SyntaxToken binaryOperatorKeyword, SyntaxToken operatorToken, SyntaxToken openParenToken, SyntaxToken rightOperandToken, TypeClauseSyntax rightOperandType, SyntaxToken closeParenToken, TypeClauseSyntax returnType, BlockStatementSyntax body) {
        super(syntaxTree);
        this.binaryOperatorKeyword = binaryOperatorKeyword;
        this.operatorToken = operatorToken;
        this.openParenToken = openParenToken;
        this.rightOperandToken = rightOperandToken;
        this.rightOperandType = rightOperandType;
        this.closeParenToken = closeParenToken;
        this.returnType = returnType;
        this.body = body;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.TYPE_BINARY_OPERATOR_DECLARATION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(binaryOperatorKeyword, operatorToken, openParenToken, rightOperandToken, rightOperandType, closeParenToken, returnType, body);
    }
}
