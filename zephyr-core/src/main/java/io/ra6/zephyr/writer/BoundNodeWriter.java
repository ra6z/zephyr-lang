package io.ra6.zephyr.writer;

import io.ra6.zephyr.IndentedWriter;
import io.ra6.zephyr.codeanalysis.binding.BoundNode;
import io.ra6.zephyr.codeanalysis.binding.BoundNodeKind;
import io.ra6.zephyr.codeanalysis.binding.BoundStatement;
import io.ra6.zephyr.codeanalysis.binding.expressions.*;
import io.ra6.zephyr.codeanalysis.binding.statements.*;
import io.ra6.zephyr.codeanalysis.symbols.FunctionSymbol;
import io.ra6.zephyr.codeanalysis.symbols.Symbol;
import io.ra6.zephyr.codeanalysis.symbols.VariableSymbol;

import java.io.PrintWriter;

public class BoundNodeWriter {
    public static void writeNode(PrintWriter writer, BoundNode node) {
        if (writer instanceof IndentedWriter indentedWriter) {
            writeNode(indentedWriter, node);
        } else {
            writeNode(new IndentedWriter(writer), node);
        }
    }

    private static void writeNode(IndentedWriter writer, BoundNode node) {
        switch (node.getKind()) {
            case BLOCK_STATEMENT -> writeBlockStatement(writer, (BoundBlockStatement) node);
            case VARIABLE_DECLARATION -> writeVariableDeclaration(writer, (BoundVariableDeclaration) node);
            case IF_STATEMENT -> writeIfStatement(writer, (BoundIfStatement) node);
            case WHILE_STATEMENT -> writeWhileStatement(writer, (BoundWhileStatement) node);
            case LABEL_STATEMENT -> writeLabelStatement(writer, (BoundLabelStatement) node);
            case GOTO_STATEMENT -> writeGotoStatement(writer, (BoundGotoStatement) node);
            case CONDITIONAL_GOTO_STATEMENT ->
                    writeConditionalGotoStatement(writer, (BoundConditionalGotoStatement) node);
            case RETURN_STATEMENT -> writeReturnStatement(writer, (BoundReturnStatement) node);
            case EXPRESSION_STATEMENT -> writeExpressionStatement(writer, (BoundExpressionStatement) node);
            case ERROR_EXPRESSION -> writeErrorExpression(writer, (BoundErrorExpression) node);
            case LITERAL_EXPRESSION -> writeLiteralExpression(writer, (BoundLiteralExpression) node);
            case VARIABLE_EXPRESSION -> writeVariableExpression(writer, (BoundVariableExpression) node);
            case ASSIGNMENT_EXPRESSION -> writeAssignmentExpression(writer, (BoundAssignmentExpression) node);
            case UNARY_EXPRESSION -> writeUnaryExpression(writer, (BoundUnaryExpression) node);
            case BINARY_EXPRESSION -> writeBinaryExpression(writer, (BoundBinaryExpression) node);
            case FUNCTION_CALL_EXPRESSION -> writeFunctionCallExpression(writer, (BoundFunctionCallExpression) node);
            case CONDITIONAL_EXPRESSION -> writeConditionalExpression(writer, (BoundConditionalExpression) node);
            case FIELD_ACCESS_EXPRESSION -> writeFieldAccessExpression(writer, (BoundFieldAccessExpression) node);
            case MEMBER_ACCESS_EXPRESSION -> writeMemberAccessExpression(writer, (BoundMemberAccessExpression) node);
            case THIS_EXPRESSION -> writeThisExpression(writer, (BoundThisExpression) node);
            case INSTANCE_CREATION_EXPRESSION ->
                    writeInstanceCreationExpression(writer, (BoundInstanceCreationExpression) node);
            case TYPE_EXPRESSION -> writeTypeExpression(writer, (BoundTypeExpression) node);
            default -> throw new RuntimeException("Unexpected node kind: " + node.getKind());
        }
    }

    private static void writeNestedStatement(IndentedWriter writer, BoundNode node) {
        boolean needsIndentation = node.getKind() != BoundNodeKind.BLOCK_STATEMENT;
        if (needsIndentation) {
            writer.indent();
        }

        writeNode(writer, node);

        if (needsIndentation) {
            writer.unindent();
        }
    }

    private static void writeBlockStatement(IndentedWriter writer, BoundBlockStatement node) {
        writer.println("{");
        writer.indent();
        for (BoundStatement s : node.getStatements()) {
            writeNestedStatement(writer, s);
        }
        writer.unindent();
        writer.println("}");
    }

    private static void writeVariableDeclaration(IndentedWriter writer, BoundVariableDeclaration node) {
        writer.print(node.getVariableSymbol().isReadonly() ? "const" : "var");
        writer.print(" ");
        writer.print(node.getVariableSymbol().getName());
        writer.print(" = ");
        writeNode(writer, node.getInitializer());
        writer.println(";");
    }

    private static void writeIfStatement(IndentedWriter writer, BoundIfStatement node) {
        writer.print("if (");
        writeNode(writer, node.getCondition());
        writer.println(")");
        writeNestedStatement(writer, node.getThenStatement());
        if (node.getElseStatement() != null) {
            writer.println("else");
            writeNestedStatement(writer, node.getElseStatement());
        }
    }

    private static void writeWhileStatement(IndentedWriter writer, BoundWhileStatement node) {
        writer.print("while (");
        writeNode(writer, node.getCondition());
        writer.println(")");
        writeNestedStatement(writer, node.getBody());
    }

    private static void writeLabelStatement(IndentedWriter writer, BoundLabelStatement node) {
        boolean unindent = writer.getIndentLevel() > 0;
        if (unindent) {
            writer.unindent();
        }

        writer.print(node.getLabel().getName());
        writer.println(":");

        if (unindent) {
            writer.indent();
        }
    }

    private static void writeGotoStatement(IndentedWriter writer, BoundGotoStatement node) {
        writer.print("goto ");
        writer.print(node.getLabel().getName());
        writer.println(";");
    }

    private static void writeConditionalGotoStatement(IndentedWriter writer, BoundConditionalGotoStatement node) {
        writer.print("goto");
        writer.print(" ");
        writer.print(node.getLabel().getName());
        writer.print(node.jumpIfTrue() ? " if " : " unless ");
        writeNode(writer, node.getCondition());
        writer.println(";");
    }

    private static void writeReturnStatement(IndentedWriter writer, BoundReturnStatement node) {
        writer.print("return");
        if (node.getExpression() != null) {
            writer.print(" ");
            writeNode(writer, node.getExpression());
        }
        writer.println(";");
    }

    private static void writeExpressionStatement(IndentedWriter writer, BoundExpressionStatement node) {
        writeNode(writer, node.getExpression());
        writer.println(";");
    }

    private static void writeErrorExpression(IndentedWriter writer, BoundErrorExpression node) {
        writer.print("<ERROR>");
    }

    private static void writeLiteralExpression(IndentedWriter writer, BoundLiteralExpression node) {
        writer.print(node.getValue());
    }

    private static void writeVariableExpression(IndentedWriter writer, BoundVariableExpression node) {
        writer.print(node.getVariable().getName());
    }

    private static void writeAssignmentExpression(IndentedWriter writer, BoundAssignmentExpression node) {
        writeNode(writer, node.getTarget());
        writer.print(" = ");
        writeNode(writer, node.getExpression());
    }

    private static void writeUnaryExpression(IndentedWriter writer, BoundUnaryExpression node) {
        writer.print(node.getOperator());
        writeNode(writer, node.getOperand());
    }

    private static void writeBinaryExpression(IndentedWriter writer, BoundBinaryExpression node) {
        writeNode(writer, node.getLeft());
        writer.print(" ");
        writer.print(node.getOperator());
        writer.print(" ");
        writeNode(writer, node.getRight());
    }

    private static void writeFunctionCallExpression(IndentedWriter writer, BoundFunctionCallExpression node) {
        writeNode(writer, node.getCallee());
        writer.print("(");
        for (int i = 0; i < node.getArguments().size(); i++) {
            if (i > 0) {
                writer.print(", ");
            }
            writeNode(writer, node.getArguments().get(i));
        }
        writer.print(")");
    }

    private static void writeConditionalExpression(IndentedWriter writer, BoundConditionalExpression node) {
        writeNode(writer, node.getCondition());
        writer.print(" ? ");
        writeNode(writer, node.getThenExpression());
        writer.print(" : ");
        writeNode(writer, node.getElseExpression());
    }

    private static void writeFieldAccessExpression(IndentedWriter writer, BoundFieldAccessExpression node) {
        writeNode(writer, node.getTarget());
    }

    private static void writeMemberAccessExpression(IndentedWriter writer, BoundMemberAccessExpression node) {
        writeNode(writer, node.getTarget());
        writer.print(".");
        writeSymbol(writer, node.getMember());
    }

    private static void writeSymbol(IndentedWriter writer, Symbol member) {
        if (member instanceof VariableSymbol) {
            writer.print(member.getName());
        } else if (member instanceof FunctionSymbol) {
            writer.print(member.getName());
        } else {
            throw new IllegalArgumentException("Unexpected symbol type.");
        }
    }

    private static void writeThisExpression(IndentedWriter writer, BoundThisExpression node) {
        writer.print("this");
    }

    private static void writeInstanceCreationExpression(IndentedWriter writer, BoundInstanceCreationExpression node) {
        writer.print("new ");
        writer.print(node.getType().getName());
        writer.print("(");
        for (int i = 0; i < node.getArguments().size(); i++) {
            if (i > 0) {
                writer.print(", ");
            }
            writeNode(writer, node.getArguments().get(i));
        }
        writer.print(")");
    }

    private static void writeTypeExpression(IndentedWriter writer, BoundTypeExpression node) {
        writer.print(node.getType().getName());
    }
}
