package io.ra6.zephyr.codeanalysis.binding.expressions;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.builtin.BuiltinTypes;
import io.ra6.zephyr.codeanalysis.symbols.*;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import lombok.Getter;

public class BoundMemberAccessExpression extends BoundExpression {
    @Getter
    private final BoundExpression target;
    @Getter
    private final Symbol member;

    public BoundMemberAccessExpression(SyntaxNode syntax, BoundExpression target, Symbol member) {
        super(syntax);
        this.target = target;
        this.member = member;
    }

    @Override
    public TypeSymbol getType() {
        if (member instanceof FieldSymbol) {
            return ((FieldSymbol) member).getType();
        }
        if (member instanceof VariableSymbol) {
            return ((VariableSymbol) member).getType();
        }
        if (member instanceof FunctionSymbol) {
            return ((FunctionSymbol) member).getType();
        }
        return BuiltinTypes.ERROR;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.MEMBER_ACCESS_EXPRESSION;
    }
}
