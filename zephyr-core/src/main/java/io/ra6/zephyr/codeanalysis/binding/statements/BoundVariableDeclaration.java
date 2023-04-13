package io.ra6.zephyr.codeanalysis.binding.statements;

import io.ra6.zephyr.codeanalysis.binding.BoundExpression;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.binding.BoundStatement;
import io.ra6.zephyr.codeanalysis.symbols.VariableSymbol;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import io.ra6.zephyr.codeanalysis.syntax.statements.VariableDeclarationSyntax;
import lombok.Getter;

public class BoundVariableDeclaration extends BoundStatement {
    @Getter
    private final VariableSymbol variableSymbol;
    @Getter
    private final BoundExpression initializer;

    public BoundVariableDeclaration(SyntaxNode syntax, VariableSymbol variableSymbol, BoundExpression initializer) {
        super(syntax);
        this.variableSymbol = variableSymbol;
        this.initializer = initializer;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.VARIABLE_DECLARATION;
    }
}
