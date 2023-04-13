package io.ra6.zephyr.sourcefile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TextLine {
    @Getter
    private final SourceText text;
    @Getter
    private final int start;
    @Getter
    private final int length;

    public int getEnd() {
        return start + length;
    }

    public TextSpan getSpan() {
        return new TextSpan(start, length);
    }

    @Override
    public String toString() {
        return text.substring(getSpan());
    }
}
