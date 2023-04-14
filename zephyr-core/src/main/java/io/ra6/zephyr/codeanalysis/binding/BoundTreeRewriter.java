package io.ra6.zephyr.codeanalysis.binding;

import io.ra6.zephyr.codeanalysis.binding.expressions.*;
import io.ra6.zephyr.codeanalysis.binding.statements.*;

import java.util.ArrayList;
import java.util.List;

public abstract class BoundTreeRewriter {
    public BoundStatement rewriteStatement(BoundStatement node) {
        return switch (node.getKind()) {
            case BLOCK_STATEMENT -> rewriteBlockStatement((BoundBlockStatement) node);
            case EXPRESSION_STATEMENT -> rewriteExpressionStatement((BoundExpressionStatement) node);
            case IF_STATEMENT -> rewriteIfStatement((BoundIfStatement) node);
            case VARIABLE_DECLARATION -> rewriteVariableDeclaration((BoundVariableDeclaration) node);
            case WHILE_STATEMENT -> rewriteWhileStatement((BoundWhileStatement) node);
            //case FOR_STATEMENT -> rewriteForStatement((BoundForStatement) node);
            //case DO_WHILE_STATEMENT -> rewriteDoWhileStatement((BoundDoWhileStatement) node);
            case GOTO_STATEMENT -> rewriteGotoStatement((BoundGotoStatement) node);
            case LABEL_STATEMENT -> rewriteLabelStatement((BoundLabelStatement) node);
            case RETURN_STATEMENT -> rewriteReturnStatement((BoundReturnStatement) node);
            case CONDITIONAL_GOTO_STATEMENT -> rewriteConditionalGotoStatement((BoundConditionalGotoStatement) node);

            default -> throw new IllegalArgumentException("Cannot rewrite " + node.getKind());
        };
    }

    protected BoundStatement rewriteConditionalGotoStatement(BoundConditionalGotoStatement node) {
        BoundExpression condition = rewriteExpression(node.getCondition());
        if (condition == node.getCondition())
            return node;

        return new BoundConditionalGotoStatement(node.getSyntax(), condition, node.getLabel(), node.jumpIfTrue());
    }

    protected BoundStatement rewriteReturnStatement(BoundReturnStatement node) {
        BoundExpression expression = node.getExpression() == null ? null : rewriteExpression(node.getExpression());
        if (expression == node.getExpression())
            return node;

        return new BoundReturnStatement(node.getSyntax(), expression);
    }

    protected BoundStatement rewriteLabelStatement(BoundLabelStatement node) {
        return node;
    }

    protected BoundStatement rewriteGotoStatement(BoundGotoStatement node) {
        return node;
    }

    protected BoundStatement rewriteWhileStatement(BoundWhileStatement node) {
        BoundExpression condition = rewriteExpression(node.getCondition());
        BoundStatement body = rewriteStatement(node.getBody());

        if (condition == node.getCondition() && body == node.getBody())
            return node;

        return new BoundWhileStatement(node.getSyntax(), condition, body, node.getBreakLabel(), node.getContinueLabel());
    }

    protected BoundStatement rewriteVariableDeclaration(BoundVariableDeclaration node) {
        BoundExpression initializer = rewriteExpression(node.getInitializer());
        if (initializer == node.getInitializer())
            return node;

        return new BoundVariableDeclaration(node.getSyntax(), node.getVariableSymbol(), initializer);
    }


    protected BoundStatement rewriteIfStatement(BoundIfStatement node) {
        BoundExpression condition = rewriteExpression(node.getCondition());
        BoundStatement thenStatement = rewriteStatement(node.getThenStatement());
        BoundStatement elseStatement = rewriteStatement(node.getElseStatement());

        if (condition == node.getCondition() &&
                thenStatement == node.getThenStatement() &&
                elseStatement == node.getElseStatement()) {
            return node;
        }

        return new BoundIfStatement(node.getSyntax(), condition, thenStatement, elseStatement);
    }

    protected BoundStatement rewriteExpressionStatement(BoundExpressionStatement node) {
        BoundExpression expression = rewriteExpression(node.getExpression());
        if (expression == node.getExpression())
            return node;

        return new BoundExpressionStatement(node.getSyntax(), expression);
    }

    protected BoundStatement rewriteBlockStatement(BoundBlockStatement node) {
        List<BoundStatement> statements = null;

        for (int i = 0; i < node.getStatements().size(); i++) {
            BoundStatement oldStatement = node.getStatements().get(i);
            BoundStatement newStatement = rewriteStatement(oldStatement);

            if (newStatement != oldStatement) {
                if (statements == null) {
                    statements = new ArrayList<>(node.getStatements().subList(0, i));
                }

                statements.add(newStatement);
            } else if (statements != null) {
                statements.add(oldStatement);
            }
        }

        if (statements == null) {
            return node;
        }

        return new BoundBlockStatement(node.getSyntax(), statements);
    }

    private BoundExpression rewriteExpression(BoundExpression node) {
        return switch (node.getKind()) {
            case ERROR_EXPRESSION -> rewriteErrorExpression((BoundErrorExpression) node);
            case LITERAL_EXPRESSION -> rewriteLiteralExpression((BoundLiteralExpression) node);
            case VARIABLE_EXPRESSION -> rewriteVariableExpression((BoundVariableExpression) node);
            case ASSIGNMENT_EXPRESSION -> rewriteAssignmentExpression((BoundAssignmentExpression) node);
            case UNARY_EXPRESSION -> rewriteUnaryExpression((BoundUnaryExpression) node);
            case BINARY_EXPRESSION -> rewriteBinaryExpression((BoundBinaryExpression) node);
            case METHOD_CALL_EXPRESSION -> rewriteMethodCallExpression((BoundMethodCallExpression) node);
            case INSTANCE_CREATION_EXPRESSION ->
                    rewriteInstanceCreationExpression((BoundInstanceCreationExpression) node);
            case MEMBER_ACCESS_EXPRESSION -> rewriteMemberAccessExpression((BoundMemberAccessExpression) node);
            case FIELD_ACCESS_EXPRESSION -> rewriteFieldAccessExpression((BoundFieldAccessExpression) node);
            case ARRAY_LITERAL_EXPRESSION -> rewriteArrayLiteralExpression((BoundArrayLiteralExpression) node);
            case ARRAY_ACCESS_EXPRESSION -> rewriteArrayAccessExpression((BoundArrayAccessExpression) node);
            case ARRAY_CREATION_EXPRESSION -> rewriteArrayCreationExpression((BoundArrayCreationExpression) node);
            case CONVERSION_EXPRESSION -> rewriteConversionExpression((BoundConversionExpression) node);
            default -> throw new IllegalArgumentException("Cannot rewrite " + node.getKind());
        };
    }

    private BoundExpression rewriteConversionExpression(BoundConversionExpression node) {
        BoundExpression expression = rewriteExpression(node.getExpression());
        if (expression == node.getExpression())
            return node;
        return node;
    }

    private BoundExpression rewriteArrayCreationExpression(BoundArrayCreationExpression node) {
        List<BoundExpression> sizes = null;

        for (int i = 0; i < node.getDimensions().size(); i++) {
            BoundExpression oldSize = node.getDimensions().get(i);
            BoundExpression newSize = rewriteExpression(oldSize);

            if (newSize != oldSize) {
                if (sizes == null) {
                    sizes = new ArrayList<>(node.getDimensions().subList(0, i));
                }

                sizes.add(newSize);
            } else if (sizes != null) {
                sizes.add(oldSize);
            }
        }

        if (sizes == null) {
            return node;
        }

        return new BoundArrayCreationExpression(node.getSyntax(), node.getType(), sizes);
    }

    private BoundExpression rewriteArrayAccessExpression(BoundArrayAccessExpression node) {
        return node;
    }

    private BoundExpression rewriteArrayLiteralExpression(BoundArrayLiteralExpression node) {
        List<BoundExpression> arguments = null;

        for (int i = 0; i < node.getElements().size(); i++) {
            BoundExpression oldArgument = node.getElements().get(i);
            BoundExpression newArgument = rewriteExpression(oldArgument);

            if (newArgument != oldArgument) {
                if (arguments == null) {
                    arguments = new ArrayList<>(node.getElements().subList(0, i));
                }

                arguments.add(newArgument);
            } else if (arguments != null) {
                arguments.add(oldArgument);
            }
        }

        if (arguments == null) {
            return node;
        }

        return node;
    }

    private BoundExpression rewriteFieldAccessExpression(BoundFieldAccessExpression node) {
        return node;
    }

    private BoundExpression rewriteMemberAccessExpression(BoundMemberAccessExpression node) {
        return node;
    }

    private BoundExpression rewriteInstanceCreationExpression(BoundInstanceCreationExpression node) {
        List<BoundExpression> arguments = null;

        for (int i = 0; i < node.getArguments().size(); i++) {
            BoundExpression oldArgument = node.getArguments().get(i);
            BoundExpression newArgument = rewriteExpression(oldArgument);

            if (newArgument != oldArgument) {
                if (arguments == null) {
                    arguments = new ArrayList<>(node.getArguments().subList(0, i));
                }

                arguments.add(newArgument);
            } else if (arguments != null) {
                arguments.add(oldArgument);
            }
        }

        if (arguments == null) {
            return node;
        }

        return new BoundInstanceCreationExpression(node.getSyntax(), node.getType(), arguments, node.getGenericTypes());
    }

    protected BoundExpression rewriteMethodCallExpression(BoundMethodCallExpression node) {
        List<BoundExpression> arguments = null;

        for (int i = 0; i < node.getArguments().size(); i++) {
            BoundExpression oldArgument = node.getArguments().get(i);
            BoundExpression newArgument = rewriteExpression(oldArgument);

            if (newArgument != oldArgument) {
                if (arguments == null) {
                    arguments = new ArrayList<>(node.getArguments().subList(0, i));
                }

                arguments.add(newArgument);
            } else if (arguments != null) {
                arguments.add(oldArgument);
            }
        }

        if (arguments == null) {
            return node;
        }

        return new BoundMethodCallExpression(node.getSyntax(), node.getCallee(), node.getFunction(), arguments);
    }

    protected BoundExpression rewriteBinaryExpression(BoundBinaryExpression node) {
        BoundExpression left = rewriteExpression(node.getLeft());
        BoundExpression right = rewriteExpression(node.getRight());

        if (left == node.getLeft() && right == node.getRight())
            return node;

        return new BoundBinaryExpression(node.getSyntax(), left, node.getOperator(), right, node.getType());
    }

    protected BoundExpression rewriteUnaryExpression(BoundUnaryExpression node) {
        BoundExpression operand = rewriteExpression(node.getOperand());
        if (operand == node.getOperand())
            return node;

        return new BoundUnaryExpression(node.getSyntax(), node.getOperator(), operand, node.getType());
    }

    protected BoundExpression rewriteAssignmentExpression(BoundAssignmentExpression node) {
        BoundExpression expression = rewriteExpression(node.getExpression());
        if (expression == node.getExpression())
            return node;

        return new BoundAssignmentExpression(node.getSyntax(), node.getTarget(), expression);
    }

    protected BoundExpression rewriteVariableExpression(BoundVariableExpression node) {
        return node;
    }

    protected BoundExpression rewriteLiteralExpression(BoundLiteralExpression node) {
        return node;
    }

    protected BoundExpression rewriteErrorExpression(BoundErrorExpression node) {
        return node;
    }
}
