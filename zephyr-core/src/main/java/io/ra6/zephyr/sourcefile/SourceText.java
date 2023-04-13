package io.ra6.zephyr.sourcefile;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class SourceText {
    @Getter
    private final String text;
    @Getter
    private final TextLine[] lines;
    @Getter
    private final String filePath;

    private final String lineSplitter;

    private SourceText(String text, String filePath) {
        this.text = text;

        this.lineSplitter = text.contains("\r\n") ? "\r\n" : text.contains("\n") ? "\n" : "\r";

        this.lines = parseLines(text.split(lineSplitter));
        this.filePath = filePath;
    }

    private TextLine[] parseLines(String[] splitRaw) {
        List<TextLine> lines = new ArrayList<>();
        int lineSplitterWidth = lineSplitter.length();

        int position = 0;

        for (String line : splitRaw) {
            TextLine tl = new TextLine(this, position, line.length());
            position += line.length();
            position += lineSplitterWidth;

            lines.add(tl);
        }


        return lines.toArray(new TextLine[0]);
    }

    public static SourceText fromString(String text) {
        return new SourceText(text, "");
    }

    public static SourceText fromFile(String filePath) throws IOException {
        Path path = Path.of(filePath);

        if (!Files.exists(path))
            throw new IOException("Cannot load source file %s, because the file does not exist.".formatted(filePath));

        List<String> lines = Files.readAllLines(path);
        String text = String.join(System.lineSeparator(), lines);
        return new SourceText(text, filePath);
    }

    public int getLineCount() {
        return lines.length;
    }

    public int getLength() {
        return text.length();
    }

    public char charAt(int index) {
        return text.charAt(index);
    }

    public TextLine getLineAt(int lineIndex) {
        return lines[lineIndex];
    }

    /**
     * Returns the line index for a position. It's zero indexed
     *
     * @param position This is between 0 and the source file length
     * @return The line for a position
     */
    public int getLineIndex(int position) {
        int idx = 0;
        for (int i = 0; i < lines.length; i++) {
            String line = "%s%s".formatted(lines[i], lineSplitter);

            for (int c = 0; c < line.length(); c++) {
                if (position == idx++) {
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * Returns the column index for a position. It's zero indexed
     *
     * @param position This is between 0 and the source file length
     * @return The column for a position
     */
    public TextFilePosition getSourcePosition(int position) {
        int line = getLineIndex(position);
        int column = getColumnIndex(position);

        return new TextFilePosition(line, column);
    }

    private int getColumnIndex(int position) {
        int idx = 0;
        int col = 0;

        for (TextLine s : lines) {
            String line = "%s%s".formatted(s, lineSplitter);
            col = 0;

            for (int c = 0; c < line.length(); c++) {
                idx++;
                col++;

                if (position == idx) {
                    return col;
                }
            }
        }
        return 0;
    }

    public String substring(int start, int length) {
        return text.substring(start, start + length);
    }

    public String substring(TextSpan span) {
        return substring(span.getStart(), span.getLength());
    }
}
