package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.List;

public class MemberAccessExpressionSyntax extends ExpressionSyntax {
    @Getter
    private final ExpressionSyntax target;
    @Getter
    private final SyntaxToken dotToken;
    @Getter
    private final SyntaxToken member;

    public MemberAccessExpressionSyntax(SyntaxTree syntaxTree, ExpressionSyntax target, SyntaxToken dotToken, SyntaxToken member) {
        super(syntaxTree);
        this.target = target;
        this.dotToken = dotToken;
        this.member = member;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.MEMBER_ACCESS_EXPRESSION;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(target, dotToken, member);
    }
}
