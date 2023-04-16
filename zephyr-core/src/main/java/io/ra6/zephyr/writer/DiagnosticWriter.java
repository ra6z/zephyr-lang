package io.ra6.zephyr.writer;

import io.ra6.zephyr.ConsoleColors;
import io.ra6.zephyr.diagnostic.Diagnostic;
import io.ra6.zephyr.diagnostic.DiagnosticBag;
import io.ra6.zephyr.sourcefile.SourceText;
import io.ra6.zephyr.sourcefile.TextLine;
import io.ra6.zephyr.sourcefile.TextSpan;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;


// TODO: Better diagnostics

public final class DiagnosticWriter {
    public static void printDiagnostics(PrintStream stream, DiagnosticBag diagnostics) {
        printDiagnostics(stream, diagnostics.asList());
    }

    public static void printDiagnostics(PrintStream stream, List<Diagnostic> diagnostics) {
        for (Diagnostic diagnostic : diagnostics.stream()
                .filter(d -> d.getLocation().getSource() == null)
                .toList()) {

            String messageColor = diagnostic.isWarning() ? ConsoleColors.ANSI_YELLOW : ConsoleColors.ANSI_RED;
            stream.print(messageColor);
            stream.print(diagnostic.getMessage());
            stream.println(ConsoleColors.ANSI_RESET);
        }

        for (Diagnostic diagnostic : diagnostics.stream()
                .filter(d -> d.getLocation().getSource() != null)
                .sorted(Comparator.comparing(o -> o.getLocation().getSource().getFilePath()))
                .toList()) {

            SourceText text = diagnostic.getLocation().getSource();
            String filePath = diagnostic.getLocation().getFilePath();
            int startLine = diagnostic.getLocation().getStartLine() + 1;
            int endLine = diagnostic.getLocation().getEndLine() + 1;

            TextSpan span = diagnostic.getLocation().getSpan();
            int lineIndex = text.getLineIndex(span.getStart());
            TextLine line = text.getLineAt(lineIndex);

            stream.println();

            String color = "";

            if (diagnostic.isError()) {
                stream.print(ConsoleColors.ANSI_RED);
                color = ConsoleColors.ANSI_RED;
                stream.print("error");
            } else {
                stream.print(ConsoleColors.ANSI_BRIGHT_YELLOW);
                color = ConsoleColors.ANSI_BRIGHT_YELLOW;
                stream.print("warning");
            }

            stream.print(ConsoleColors.ANSI_RESET);
            stream.printf(": %s", diagnostic.getMessage());
            stream.println();

            stream.print(ConsoleColors.ANSI_BLUE);
            stream.print("--> ");
            stream.print(ConsoleColors.ANSI_RESET);

            stream.printf("%s:%d:%d", filePath, startLine, span.getStart() - line.getStart() + 1);
            stream.println(ConsoleColors.ANSI_RESET);

            TextSpan prefixSpan = TextSpan.fromBounds(line.getStart(), span.getStart());
            TextSpan suffixSpan = TextSpan.fromBounds(span.getEnd(), line.getEnd());

            String prefix = text.substring(prefixSpan);
            String error = text.substring(span);
            String suffix = text.substring(suffixSpan);

            stream.print("    ");
            stream.print(prefix);

            stream.print(ConsoleColors.ANSI_RED);
            stream.print(error);
            stream.print(ConsoleColors.ANSI_RESET);

            stream.print(suffix);
            stream.println();

            stream.print(color);
            stream.print("    " + " ".repeat(prefix.length()) + "^".repeat(error.length()) + " ");
            stream.print(diagnostic.getHint());
            stream.println(ConsoleColors.ANSI_RESET);
            stream.println();
        }
    }


}
