package io.ra6.zephyr.codeanalysis.binding;

import io.ra6.zephyr.builtin.IFunctionBase;
import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.codeanalysis.binding.expressions.BoundInternalFunctionExpression;
import io.ra6.zephyr.codeanalysis.binding.expressions.BoundLiteralExpression;
import io.ra6.zephyr.codeanalysis.binding.expressions.BoundThisExpression;
import io.ra6.zephyr.codeanalysis.binding.expressions.BoundVariableExpression;
import io.ra6.zephyr.codeanalysis.binding.statements.*;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.symbols.VariableSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;

import java.util.List;

public class BoundNodeFactory {
    private BoundNodeFactory() {
    }

    public static BoundBlockStatement createBlockStatement(SyntaxNode node, BoundStatement... statements) {
        return new BoundBlockStatement(node, List.of(statements));
    }

    public static BoundStatement createGotoFalse(SyntaxNode syntax, BoundExpression condition, BoundLabel label) {
        return new BoundConditionalGotoStatement(syntax, condition, label, false);
    }

    public static BoundStatement createLabel(SyntaxNode syntax, BoundLabel label) {
        return new BoundLabelStatement(syntax, label);
    }

    public static BoundStatement createGoto(SyntaxNode syntax, BoundLabel label) {
        return new BoundGotoStatement(syntax, label);
    }

    public static BoundStatement createGotoTrue(SyntaxNode syntax, BoundExpression condition, BoundLabel label) {
        return new BoundConditionalGotoStatement(syntax, condition, label, true);
    }

    public static BoundExpression createIntLiteral(SyntaxNode node, int value) {
        return new BoundLiteralExpression(node, value, Types.INT);
    }

    public static BoundExpression createVariableExpression(SyntaxNode node, VariableSymbol variable) {
        return new BoundVariableExpression(node, variable);
    }

    public static BoundExpression createInternalFunctionExpression(SyntaxNode node, IFunctionBase function, List<BoundExpression> arguments) {
        return new BoundInternalFunctionExpression(node, function, arguments);
    }

    public static BoundStatement createReturnStatement(SyntaxNode node, BoundExpression boundExpression) {
        return new BoundReturnStatement(node, boundExpression);
    }

    public static BoundExpression createDoubleLiteral(SyntaxNode node, double value) {
        return new BoundLiteralExpression(node, value, Types.DOUBLE);
    }

    public static BoundExpression createCharLiteral(SyntaxNode node, char value) {
        return new BoundLiteralExpression(node, value, Types.CHAR);
    }

    public static BoundExpression createTypeEqualsExpression(SyntaxNode node, BoundThisExpression boundThisExpression, TypeSymbol type) {
        return new BoundTypeCheckExpression(node, boundThisExpression, type);
    }
}
