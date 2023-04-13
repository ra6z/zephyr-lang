package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.symbols.TypeSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import io.ra6.zephyr.codeanalysis.syntax.expressions.InstanceCreationExpressionSyntax;
import lombok.Getter;

import java.util.List;

public class BoundInstanceCreationExpression extends BoundExpression {
    private final TypeSymbol type;
    @Getter
    private final List<BoundExpression> arguments;

    public BoundInstanceCreationExpression(SyntaxNode syntax, TypeSymbol type, List<BoundExpression> arguments) {
        super(syntax);
        this.type = type;
        this.arguments = arguments;
    }

    @Override
    public TypeSymbol getType() {
        return type;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.INSTANCE_CREATION_EXPRESSION;
    }
}
