package io.ra6.zephyr.codeanalysis.syntax.statements;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TypeFunctionDeclarationSyntax extends StatementSyntax {
    @Getter
    private final SyntaxToken visibilityToken;
    @Getter
    private final SyntaxToken sharedToken;
    @Getter
    private final SyntaxToken fncKeywordToken;
    @Getter
    private final SyntaxToken identifier;
    @Getter
    private final SyntaxToken openParenToken;
    @Getter
    private final SeparatedSyntaxList<ParameterSyntax> parameters;
    @Getter
    private final SyntaxToken closeParenToken;
    @Getter
    private final TypeClauseSyntax typeClause;
    @Getter
    private final BlockStatementSyntax body;

    public TypeFunctionDeclarationSyntax(SyntaxTree syntaxTree, SyntaxToken visibilityToken, SyntaxToken sharedToken, SyntaxToken fncKeywordToken, SyntaxToken identifier, SyntaxToken openParenToken, SeparatedSyntaxList<ParameterSyntax> parameters, SyntaxToken closeParenToken, TypeClauseSyntax typeClause, BlockStatementSyntax body) {
        super(syntaxTree);
        this.visibilityToken = visibilityToken;
        this.sharedToken = sharedToken;
        this.fncKeywordToken = fncKeywordToken;
        this.identifier = identifier;
        this.openParenToken = openParenToken;
        this.parameters = parameters;
        this.closeParenToken = closeParenToken;
        this.typeClause = typeClause;
        this.body = body;
    }

    public boolean isShared() {
        return sharedToken != null;
    }

    public boolean isPublic() {
        return visibilityToken != null && visibilityToken.getKind() == SyntaxKind.PUB_KEYWORD;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.TYPE_FUNCTION_DECLARATION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>();
        if (visibilityToken != null) result.add(visibilityToken);
        if (sharedToken != null) result.add(sharedToken);
        result.add(fncKeywordToken);
        result.add(identifier);
        result.add(openParenToken);
        result.addAll(parameters.getNodesAndSeparators());
        result.add(closeParenToken);
        result.add(typeClause);
        result.add(body);
        return result;
    }
}
