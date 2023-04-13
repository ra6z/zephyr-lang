package io.ra6.zephyr.diagnostic;

import io.ra6.zephyr.sourcefile.TextLocation;
import lombok.Getter;

public final class Diagnostic {
    @Getter
    private final boolean isError;
    @Getter
    private final TextLocation location;
    @Getter
    private final String message;
    @Getter
    private final boolean isWarning;
    @Getter
    private final String hint;

    private Diagnostic(boolean isError, TextLocation location, String message, String hint) {
        this.isError = isError;
        this.location = location;
        this.message = message;
        this.isWarning = !isError;
        this.hint = hint;
    }

    private Diagnostic(boolean isError, TextLocation location, String message) {
        this(isError, location, message, "");
    }

    @Override
    public String toString() {
        return message;
    }

    public static Diagnostic error(TextLocation location, String message)
    {
        return new Diagnostic(true, location, message);
    }

    public static Diagnostic error(TextLocation location, String message, String hint)
    {
        return new Diagnostic(true, location, message, hint);
    }

    public static Diagnostic warning(TextLocation location, String message)
    {
        return new Diagnostic(false, location, message);
    }

    public static Diagnostic warning(TextLocation location, String message, String hint)
    {
        return new Diagnostic(false, location, message, hint);
    }
}
