package io.ra6.zephyr.codeanalysis.syntax.expressions;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class InstanceCreationExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final SyntaxToken newKeyword;
    @Getter
    private final QualifiedNameSyntax qualifiedName;
    @Getter
    private final SyntaxToken leftParenthesis;
    @Getter
    private final SeparatedSyntaxList<ExpressionSyntax> arguments;
    @Getter
    private final SyntaxToken rightParenthesis;

    public InstanceCreationExpressionSyntax(SyntaxTree syntaxTree, SyntaxToken newKeyword, QualifiedNameSyntax qualifiedName, SyntaxToken leftParenthesis, SeparatedSyntaxList<ExpressionSyntax> arguments, SyntaxToken rightParenthesis) {
        super(syntaxTree);
        this.newKeyword = newKeyword;
        this.qualifiedName = qualifiedName;
        this.leftParenthesis = leftParenthesis;
        this.arguments = arguments;
        this.rightParenthesis = rightParenthesis;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.INSTANCE_CREATION_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>();
        result.add(newKeyword);
        result.add(qualifiedName);
        result.add(leftParenthesis);
        result.addAll(arguments.getNodesAndSeparators());
        result.add(rightParenthesis);
        return result;
    }
}
