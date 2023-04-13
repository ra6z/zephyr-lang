package io.ra6.zephyr.writer;

import io.ra6.zephyr.ConsoleColors;
import io.ra6.zephyr.Iterables;
import io.ra6.zephyr.codeanalysis.syntax.CompilationUnitSyntax;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxNode;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxToken;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxTree;

import java.io.PrintStream;
import java.util.List;

public final class SyntaxWriter {
    public static void printTree(PrintStream stream, SyntaxTree tree) {
        printTree(stream, tree.getRoot());
    }

    private static void printTree(PrintStream stream, CompilationUnitSyntax root) {
        stream.println(root.getKind());

        List<SyntaxNode> children = root.getChildren();
        for (int i = 0; i < children.size(); i++) {
            SyntaxNode child = children.get(i);
            printTree(stream, child, "", i == children.size() - 1);
        }
    }

    private static void printTree(PrintStream stream, SyntaxNode child, String indent, boolean isLast) {
        String marker = isLast ? "└──" : "├──";
        stream.print(indent);
        stream.print(marker);
        stream.print(child.getKind());

        if (child instanceof SyntaxToken token) {
            stream.print("(");
            if (token.getText() != null) {
                stream.print(ConsoleColors.ANSI_YELLOW + token.getText() + ConsoleColors.ANSI_RESET);
            }

            if (token.getValue() != null) {
                stream.print(" " + ConsoleColors.ANSI_GREEN + token.getValue() + ConsoleColors.ANSI_RESET);
            }
            stream.print(")");
        }

        stream.println();

        indent += isLast ? "    " : "│   ";

        SyntaxNode lastChild = Iterables.last(child.getChildren());
        for (SyntaxNode node : child.getChildren()) {
            printTree(stream, node, indent, node == lastChild);
        }
    }
}
