package io.ra6.zephyr.sourcefile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TextLocation {
    @Getter
    private final SourceText source;
    @Getter
    private final TextSpan span;

    public int getStartLine() {
        return source.getLineIndex(span.getStart());
    }

    public int getEndLine() {
        return source.getLineIndex(span.getEnd());
    }

    public String getFilePath() {
        return source.getFilePath();
    }

    @Override
    public String toString() {
        return "%s:%d:%d".formatted(getFilePath(), getStartLine() + 1, span.getStart() - source.getLineAt(getStartLine()).getStart() + 1);
    }

    public static TextLocation fromLocations(TextLocation... locations) {
        if (locations.length == 0) {
            return null;
        }

        SourceText source = locations[0].getSource();
        int start = locations[0].getSpan().getStart();
        int end = locations[locations.length - 1].getSpan().getEnd();

        return new TextLocation(source, TextSpan.fromBounds(start, end));
    }
}
