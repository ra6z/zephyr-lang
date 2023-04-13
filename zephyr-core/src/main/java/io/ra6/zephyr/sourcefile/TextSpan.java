package io.ra6.zephyr.sourcefile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TextSpan {
    @Getter
    private final int start;
    @Getter
    private final int length;

    public static TextSpan fromBounds(int start, int end) {
        return new TextSpan(start, end - start);
    }

    public int getEnd() {
        return start + length;
    }

    public boolean overlapsWith(TextSpan span) {
        return getStart() < span.getEnd() &&
                getEnd() > span.getStart();
    }

    @Override
    public String toString() {
        return "(%d..%d)".formatted(getStart(), getEnd());
    }
}
