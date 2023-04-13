package io.ra6.zephyr;

import lombok.Getter;

import java.io.PrintWriter;
import java.io.Writer;

public class IndentedWriter extends PrintWriter {

    @Getter
    private int indentLevel;
    private String indentString = "    "; // default indentation is four spaces

    public IndentedWriter(Writer out) {
        super(out);
    }

    public void indent() {
        indentLevel++;
    }

    public void unindent() {
        indentLevel--;
    }

    public void setIndentString(String indentString) {
        this.indentString = indentString;
    }

    public void writeIndented(String s) {
        for (int i = 0; i < indentLevel; i++) {
            super.write(indentString);
        }
        super.write(s);
    }

    public void writeIndentedLine(String s) {
        writeIndented(s);
        super.println();
    }

    @Override
    public void println() {
        super.println();
    }
}
