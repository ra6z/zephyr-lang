package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import io.ra6.zephyr.codeanalysis.syntax.statements.BlockStatementSyntax;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TypeOperatorDeclarationSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken visibilityToken;
    @Getter
    private final SyntaxToken sharedToken;
    @Getter
    private final SyntaxToken operatorKeyword;
    @Getter
    private final SyntaxToken operatorToken;
    @Getter
    private final SyntaxToken openParenToken;
    @Getter
    private final SeparatedSyntaxList<ParameterSyntax> parameters;
    @Getter
    private final SyntaxToken closeParenToken;
    @Getter
    private final TypeClauseSyntax returnType;
    @Getter
    private final BlockStatementSyntax body;

    public TypeOperatorDeclarationSyntax(SyntaxTree syntaxTree, SyntaxToken visibilityToken, SyntaxToken sharedToken, SyntaxToken operatorKeyword, SyntaxToken operatorToken, SyntaxToken openParenToken, SeparatedSyntaxList<ParameterSyntax> parameters, SyntaxToken closeParenToken, TypeClauseSyntax returnType, BlockStatementSyntax body) {
        super(syntaxTree);
        this.visibilityToken = visibilityToken;
        this.sharedToken = sharedToken;
        this.operatorKeyword = operatorKeyword;
        this.operatorToken = operatorToken;
        this.openParenToken = openParenToken;
        this.parameters = parameters;
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
        List<SyntaxNode> result = new ArrayList<>();
        result.add(visibilityToken);
        result.add(sharedToken);
        result.add(operatorKeyword);
        result.add(operatorToken);
        result.add(openParenToken);
        result.addAll(parameters.getNodesAndSeparators());
        result.add(closeParenToken);
        result.add(returnType);
        result.add(body);
        return result;
    }
}
