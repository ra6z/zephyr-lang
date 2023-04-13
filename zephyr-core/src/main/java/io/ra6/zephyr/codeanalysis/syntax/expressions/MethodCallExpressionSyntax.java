package io.ra6.zephyr.codeanalysis.syntax.expressions;

import io.ra6.zephyr.codeanalysis.syntax.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class MethodCallExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final ExpressionSyntax callee;
    @Getter
    private final SyntaxToken leftParenthesisToken;
    @Getter
    private final SeparatedSyntaxList<ExpressionSyntax> arguments;
    @Getter
    private final SyntaxToken rightParenthesisToken;

    public MethodCallExpressionSyntax(SyntaxTree syntaxTree, ExpressionSyntax callee, SyntaxToken leftParenthesisToken, SeparatedSyntaxList<ExpressionSyntax> arguments, SyntaxToken rightParenthesisToken) {
        super(syntaxTree);
        this.callee = callee;
        this.leftParenthesisToken = leftParenthesisToken;
        this.arguments = arguments;
        this.rightParenthesisToken = rightParenthesisToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.METHOD_CALL_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        List<SyntaxNode> result = new ArrayList<>();
        result.add(callee);
        result.add(leftParenthesisToken);
        result.addAll(arguments.getNodesAndSeparators());
        result.add(rightParenthesisToken);
        return result;
    }
}
