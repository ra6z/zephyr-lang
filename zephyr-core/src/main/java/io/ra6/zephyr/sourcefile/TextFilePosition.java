package io.ra6.zephyr.sourcefile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public final class TextFilePosition {
    @Getter
    private final int line;
    @Getter
    private final int column;
}
