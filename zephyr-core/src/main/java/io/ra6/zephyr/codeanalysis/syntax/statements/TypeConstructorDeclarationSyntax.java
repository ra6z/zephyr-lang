package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import io.ra6.zephyr.codeanalysis.syntax.statements.BlockStatementSyntax;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TypeConstructorDeclarationSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken constructorKeyword;
    @Getter
    private final SyntaxToken openParenToken;
    @Getter
    private final SeparatedSyntaxList<ParameterSyntax> parameters;
    @Getter
    private final SyntaxToken closeParenToken;
    @Getter
    private final BlockStatementSyntax body;

    public TypeConstructorDeclarationSyntax(SyntaxTree syntaxTree, SyntaxToken constructorKeyword, SyntaxToken openParenToken, SeparatedSyntaxList<ParameterSyntax> parameters, SyntaxToken closeParenToken, BlockStatementSyntax body) {
        super(syntaxTree);
        this.constructorKeyword = constructorKeyword;
        this.openParenToken = openParenToken;
        this.parameters = parameters;
        this.closeParenToken = closeParenToken;
        this.body = body;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.TYPE_CONSTRUCTOR_DECLARATION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>();
        result.add(constructorKeyword);
        result.add(openParenToken);
        result.addAll(parameters.getNodesAndSeparators());
        result.add(closeParenToken);
        result.add(body);
        return result;
    }
}
