package io.ra6.zephyr.codeanalysis.lowering;

import io.ra6.zephyr.Iterables;
import io.ra6.zephyr.codeanalysis.binding.*;
import io.ra6.zephyr.codeanalysis.binding.statements.BoundBlockStatement;
import io.ra6.zephyr.codeanalysis.binding.statements.BoundIfStatement;
import io.ra6.zephyr.codeanalysis.binding.statements.BoundReturnStatement;
import io.ra6.zephyr.codeanalysis.binding.statements.BoundWhileStatement;
import io.ra6.zephyr.builtin.BuiltinTypes;
import io.ra6.zephyr.codeanalysis.symbols.FunctionSymbol;
import lombok.experimental.ExtensionMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@ExtensionMethod({Iterables.class})
public class Lowerer extends BoundTreeRewriter {
    private int labelCount;

    private BoundLabel generateLabel() {
        return new BoundLabel("label$" + labelCount++);
    }

    public static BoundBlockStatement lower(FunctionSymbol function, BoundStatement root) {
        Lowerer lowerer = new Lowerer();
        BoundStatement result = lowerer.rewriteStatement(root);

        return flatten(function, result);
    }

    private static BoundBlockStatement flatten(FunctionSymbol function, BoundStatement statement) {
        List<BoundStatement> statements = new ArrayList<>();
        Stack<BoundStatement> stack = new Stack<>();

        stack.push(statement);

        while (!stack.isEmpty()) {
            BoundStatement current = stack.pop();

            if (current instanceof BoundBlockStatement block) {
                for (int i = block.getStatements().size() - 1; i >= 0; i--) {
                    stack.push(block.getStatements().get(i));
                }
            } else {
                statements.add(current);
            }
        }

        if (function.getType() == BuiltinTypes.VOID) {
            if (statements.size() == 0 || canFallThrough(statements.last())) {
                statements.add(new BoundReturnStatement(statement.getSyntax(), null));
            }
        }

        return new BoundBlockStatement(statement.getSyntax(), statements);
    }

    private static boolean canFallThrough(BoundStatement last) {
        return last.getKind() != BoundNodeKind.RETURN_STATEMENT
                && last.getKind() != BoundNodeKind.GOTO_STATEMENT;
    }

    // TODO: remove dead code


    /**
     * If there is no else statement:
     *
     * <pre>
     *      if &lt;condition&gt;
     *          &lt;thenStatement&gt;
     *
     * Will be rewritten to:
     *
     *      gotoFalse &lt;condition&gt; end
     *          &lt;thenStatement&gt;
     *      end:
     * </pre>
     * <p>
     * If there is an else statement:
     * <pre>
     *
     *     if &lt;condition&gt;
     *          &lt;thenStatement&gt;
     *     else
     *          &lt;elseStatement&gt;
     *
     * Will be rewritten to:
     *
     *      gotoFalse &lt;condition&gt; else
     *          &lt;thenStatement&gt;
     *      goto end
     *      else:
     *          &lt;elseStatement&gt;
     *      end:
     */
    @Override
    protected BoundStatement rewriteIfStatement(BoundIfStatement node) {
        if (node.getElseStatement() == null) {
            BoundLabel endLabel = generateLabel();

            BoundBlockStatement result = BoundNodeFactory.createBlockStatement(node.getSyntax(),
                    BoundNodeFactory.createGotoFalse(node.getSyntax(), node.getCondition(), endLabel),
                    rewriteStatement(node.getThenStatement()),
                    BoundNodeFactory.createLabel(node.getSyntax(), endLabel));

            return rewriteStatement(result);
        } else {
            BoundLabel elseLabel = generateLabel();
            BoundLabel endLabel = generateLabel();

            BoundBlockStatement result = BoundNodeFactory.createBlockStatement(node.getSyntax(),
                    BoundNodeFactory.createGotoFalse(node.getSyntax(), node.getCondition(), elseLabel),
                    rewriteStatement(node.getThenStatement()),
                    BoundNodeFactory.createGoto(node.getSyntax(), endLabel),
                    BoundNodeFactory.createLabel(node.getSyntax(), elseLabel),
                    rewriteStatement(node.getElseStatement()),
                    BoundNodeFactory.createLabel(node.getSyntax(), endLabel));

            return rewriteStatement(result);
        }
    }

    /**
     * <pre>
     *      while &lt;condition&gt;
     *          &lt;body&gt;
     *
     * Will be rewritten to:
     *
     *     goto continue
     *     body:
     *          &lt;body&gt;
     *     continue:
     *     gotoTrue &lt;condition&gt; body
     *     break:
     * </pre>
     *
     * @param node
     * @return
     */
    @Override
    protected BoundStatement rewriteWhileStatement(BoundWhileStatement node) {
        BoundLabel bodyLabel = generateLabel();
        BoundBlockStatement result = BoundNodeFactory.createBlockStatement(node.getSyntax(),
                BoundNodeFactory.createGoto(node.getSyntax(), node.getContinueLabel()),
                BoundNodeFactory.createLabel(node.getSyntax(), bodyLabel),
                rewriteStatement(node.getBody()),
                BoundNodeFactory.createLabel(node.getSyntax(), node.getContinueLabel()),
                BoundNodeFactory.createGotoTrue(node.getSyntax(), node.getCondition(), bodyLabel),
                BoundNodeFactory.createLabel(node.getSyntax(), node.getBreakLabel()));

        return rewriteStatement(result);
    }

    // TODO: Compound assignment
}
