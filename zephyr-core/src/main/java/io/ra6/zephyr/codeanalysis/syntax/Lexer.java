package io.ra6.zephyr.codeanalysis.syntax;

import io.ra6.zephyr.diagnostic.DiagnosticBag;
import io.ra6.zephyr.sourcefile.SourceText;
import io.ra6.zephyr.sourcefile.TextLocation;
import io.ra6.zephyr.sourcefile.TextSpan;
import lombok.Getter;

public final class Lexer {
    @Getter
    private final DiagnosticBag diagnostics = new DiagnosticBag();

    private final SourceText sourceText;
    private final SyntaxTree syntaxTree;
    private int currentPosition;

    private SyntaxKind currentTokenKind;
    private int currentTokenStart;
    private Object currentTokenValue;

    public Lexer(SyntaxTree syntaxTree) {
        this.syntaxTree = syntaxTree;
        this.sourceText = syntaxTree.getSourceText();
    }

    public SyntaxToken lex() {
        currentTokenStart = currentPosition;
        currentTokenKind = SyntaxKind.BAD_TOKEN;
        currentTokenValue = null;

        lexToken();

        int tokenLength = currentPosition - currentTokenStart;

        String tokenText = SyntaxFacts.getText(currentTokenKind);
        if (tokenText == null)
            tokenText = sourceText.substring(currentTokenStart, tokenLength);

        TextSpan tokenSpan = new TextSpan(currentTokenStart, tokenLength);

        return new SyntaxToken(syntaxTree, currentTokenKind, tokenSpan, tokenText, currentTokenValue);
    }

    private void lexToken() {
        switch (current()) {
            case '\0' -> currentTokenKind = SyntaxKind.END_OF_FILE_TOKEN;
            case '+' -> {
                if (lookahead() == '+') {
                    currentTokenKind = SyntaxKind.PLUS_PLUS_TOKEN;
                    currentPosition++;
                } else if (lookahead() == '=') {
                    currentTokenKind = SyntaxKind.PLUS_EQUALS_TOKEN;
                    currentPosition++;
                } else {
                    currentTokenKind = SyntaxKind.PLUS_TOKEN;
                }
                currentPosition++;
            }
            case '-' -> {
                if (lookahead() == '-') {
                    currentTokenKind = SyntaxKind.MINUS_MINUS_TOKEN;
                    currentPosition++;
                } else if (lookahead() == '=') {
                    currentTokenKind = SyntaxKind.MINUS_EQUALS_TOKEN;
                    currentPosition++;
                } else {
                    currentTokenKind = SyntaxKind.MINUS_TOKEN;
                }
                currentPosition++;
            }
            case '*' -> {
                if (lookahead() == '=') {
                    currentTokenKind = SyntaxKind.STAR_EQUALS_TOKEN;
                    currentPosition++;
                } else {
                    currentTokenKind = SyntaxKind.STAR_TOKEN;
                }
                currentPosition++;
            }
            case '/' -> {
                if (lookahead() == '=') {
                    currentTokenKind = SyntaxKind.SLASH_EQUALS_TOKEN;
                    currentPosition++;
                } else {
                    currentTokenKind = SyntaxKind.SLASH_TOKEN;
                }
                currentPosition++;
            }
            case '=' -> {
                if (lookahead() == '=') {
                    currentTokenKind = SyntaxKind.EQUALS_EQUALS_TOKEN;
                    currentPosition++;
                } else {
                    currentTokenKind = SyntaxKind.EQUALS_TOKEN;
                }
                currentPosition++;
            }
            case '(' -> {
                currentTokenKind = SyntaxKind.OPEN_PARENTHESIS_TOKEN;
                currentPosition++;
            }
            case ')' -> {
                currentTokenKind = SyntaxKind.CLOSE_PARENTHESIS_TOKEN;
                currentPosition++;
            }
            case '[' -> {
                currentTokenKind = SyntaxKind.OPEN_BRACKET_TOKEN;
                currentPosition++;
            }
            case ']' -> {
                currentTokenKind = SyntaxKind.CLOSE_BRACKET_TOKEN;
                currentPosition++;
            }
            case '{' -> {
                currentTokenKind = SyntaxKind.OPEN_BRACE_TOKEN;
                currentPosition++;
            }
            case '}' -> {
                currentTokenKind = SyntaxKind.CLOSE_BRACE_TOKEN;
                currentPosition++;
            }
            case ':' -> {
                currentTokenKind = SyntaxKind.COLON_TOKEN;
                currentPosition++;
            }
            case ';' -> {
                currentTokenKind = SyntaxKind.SEMICOLON_TOKEN;
                currentPosition++;
            }
            case ',' -> {
                currentTokenKind = SyntaxKind.COMMA_TOKEN;
                currentPosition++;
            }
            case '.' -> {
                currentTokenKind = SyntaxKind.DOT_TOKEN;
                currentPosition++;
            }
            case '%' -> {
                currentTokenKind = SyntaxKind.PERCENT_TOKEN;
                currentPosition++;
            }
            case '?' -> {
                currentTokenKind = SyntaxKind.QUESTION_TOKEN;
                currentPosition++;
            }
            case '|' -> {
                if (lookahead() == '|') {
                    currentTokenKind = SyntaxKind.PIPE_PIPE_TOKEN;
                    currentPosition++;
                } else {
                    currentTokenKind = SyntaxKind.PIPE_TOKEN;
                }
                currentPosition++;
            }
            case '&' -> {
                if (lookahead() == '&') {
                    currentTokenKind = SyntaxKind.AMPERSAND_AMPERSAND_TOKEN;
                    currentPosition++;
                } else {
                    currentTokenKind = SyntaxKind.AMPERSAND_TOKEN;
                }
                currentPosition++;
            }
            case '^' -> {
                currentTokenKind = SyntaxKind.CARET_TOKEN;
                currentPosition++;
            }
            case '!' -> {
                if (lookahead() == '=') {
                    currentTokenKind = SyntaxKind.BANG_EQUALS_TOKEN;
                    currentPosition++;
                } else {
                    currentTokenKind = SyntaxKind.BANG_TOKEN;
                }
                currentPosition++;
            }
            case '<' -> {
                if (lookahead() == '=') {
                    currentTokenKind = SyntaxKind.LESS_EQUALS_TOKEN;
                    currentPosition++;
                } else if (lookahead() == '<') {
                    currentTokenKind = SyntaxKind.LESS_LESS_TOKEN;
                    currentPosition++;
                } else {
                    currentTokenKind = SyntaxKind.LESS_TOKEN;
                }
                currentPosition++;
            }
            case '>' -> {
                if (lookahead() == '=') {
                    currentTokenKind = SyntaxKind.GREATER_EQUALS_TOKEN;
                    currentPosition++;
                } else if (lookahead() == '>') {
                    currentTokenKind = SyntaxKind.GREATER_GREATER_TOKEN;
                    currentPosition++;
                } else {
                    currentTokenKind = SyntaxKind.GREATER_TOKEN;
                }
                currentPosition++;
            }
            case '~' -> {
                currentTokenKind = SyntaxKind.TILDE_TOKEN;
                currentPosition++;
            }
            case '"' -> readString();
            case '\'' -> readChar();
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> readNumber();
            case '_' -> readIdentifierOrKeyword();
            case '\t', ' ', '\r', '\n' -> readWhitespace();
            default -> {
                if (Character.isAlphabetic(current())) {
                    readIdentifierOrKeyword();
                } else if (Character.isWhitespace(current())) {
                    readWhitespace();
                } else {
                    TextSpan span = new TextSpan(currentPosition, 1);
                    TextLocation location = new TextLocation(sourceText, span);
                    diagnostics.reportBadCharacter(location, current());
                    currentPosition++;
                }
            }
        }
    }

    private void readChar() {
        currentPosition++;
        StringBuilder sb = new StringBuilder();
        boolean done = false;

        while (!done) {
            switch (current()) {
                case '\0', '\r', '\n' -> {
                    TextSpan span = new TextSpan(currentTokenStart, 1);
                    TextLocation location = new TextLocation(sourceText, span);
                    diagnostics.reportUnterminatedChar(location);
                    done = true;
                }
                case '\\' -> {
                    currentPosition++;
                    if (current() == 'u') {
                        // Unicode escape sequence
                        currentPosition++;
                        StringBuilder unicodeDigits = new StringBuilder();
                        for (int i = 0; i < 4; i++) {
                            if (Character.digit(current(), 16) == -1) {
                                // Invalid Unicode digit
                                TextSpan span = new TextSpan(currentTokenStart, 6);
                                TextLocation location = new TextLocation(sourceText, span);
                                diagnostics.reportInvalidUnicodeEscapeSequence(location);
                                break;
                            }
                            unicodeDigits.append(current());
                            currentPosition++;
                        }
                        char escapedChar = (char) Integer.parseInt(unicodeDigits.toString(), 16);
                        sb.append(escapedChar);
                    } else {
                        // Other escape sequence
                        char escapedChar = '\0';
                        switch (current()) {
                            case '0' -> escapedChar = '\0';
                            case 'a' -> escapedChar = '\u0007';
                            case 'b' -> escapedChar = '\b';
                            case 't' -> escapedChar = '\t';
                            case 'n' -> escapedChar = '\n';
                            case 'v' -> escapedChar = '\u000b';
                            case 'f' -> escapedChar = '\f';
                            case 'r' -> escapedChar = '\r';
                            case '"' -> escapedChar = '"';
                            case '\'' -> escapedChar = '\'';
                            case '\\' -> escapedChar = '\\';
                            default -> {
                                TextSpan span = new TextSpan(currentTokenStart, 1);
                                TextLocation location = new TextLocation(sourceText, span);
                                diagnostics.reportInvalidEscapeSequence(location);
                            }
                        }
                        sb.append(escapedChar);
                        currentPosition++;
                    }
                }
                case '\'' -> {
                    currentPosition++;
                    done = true;
                }
                default -> {
                    sb.append(current());
                    currentPosition++;
                }
            }
        }

        if (sb.length() != 1) {
            TextSpan span = new TextSpan(currentTokenStart, sb.length());
            TextLocation location = new TextLocation(sourceText, span);
            diagnostics.reportInvalidCharacterLiteral(location);
        }

        currentTokenKind = SyntaxKind.CHARACTER_TOKEN;
        currentTokenValue = sb.toString();
    }

    private void readString() {
        currentPosition++;

        StringBuilder sb = new StringBuilder();
        boolean done = false;

        while (!done) {
            switch (current()) {
                case '\0', '\r', '\n' -> {
                    TextSpan span = new TextSpan(currentTokenStart, 1);
                    TextLocation location = new TextLocation(sourceText, span);
                    diagnostics.reportUnterminatedString(location);
                    done = true;
                }
                case '"' -> {
                    if (lookahead() == '"') {
                        currentPosition++;
                        sb.append('"');
                    } else {
                        currentPosition++;
                        done = true;
                    }
                }
                default -> {
                    sb.append(current());
                    currentPosition++;
                }
            }
        }

        currentTokenKind = SyntaxKind.STRING_TOKEN;
        currentTokenValue = sb.toString();
    }

    private void readWhitespace() {
        while (Character.isWhitespace(current()))
            currentPosition++;

        currentTokenKind = SyntaxKind.WHITESPACE_TOKEN;
    }

    private void readIdentifierOrKeyword() {
        while (Character.isLetterOrDigit(current()) || '_' == current())
            currentPosition++;

        int tokenLength = currentPosition - currentTokenStart;
        String tokenText = sourceText.substring(currentTokenStart, tokenLength);

        currentTokenKind = SyntaxFacts.getKeywordKind(tokenText);
    }

    private void readNumber() {
        if (current() == '0' && lookahead() == 'x') {
            currentPosition += 2;
            readHexNumber();
            return;
        }

        if (current() == '0' && lookahead() == 'b') {
            currentPosition += 2;
            readBinaryNumber();
            return;
        }

        if (current() == '0' && lookahead() == 'o') {
            currentPosition += 2;
            readOctalNumber();
            return;
        }

        while (Character.isDigit(current()))
            currentPosition++;

        // if the current char is . and the next is 0-9, then we have a floating point number
        if (current() == '.' && Character.isDigit(lookahead())) {
            currentPosition++;

            while (Character.isDigit(current()))
                currentPosition++;

            if (current() == 'e' || current() == 'E') {
                currentPosition++;
                if (current() == '+' || current() == '-')
                    currentPosition++;
                while (Character.isDigit(current()))
                    currentPosition++;
            }
        }

        int length = currentPosition - currentTokenStart;
        String text = sourceText.substring(currentTokenStart, length);

        try {
            if (text.contains(".")) {
                currentTokenKind = SyntaxKind.FLOATING_POINT_TOKEN;
                currentTokenValue = Double.parseDouble(text);
            } else {
                currentTokenKind = SyntaxKind.NUMBER_TOKEN;
                currentTokenValue = Integer.parseInt(text);
            }
        } catch (NumberFormatException ignored) {
            TextSpan span = new TextSpan(currentTokenStart, length);
            TextLocation location = new TextLocation(sourceText, span);
            diagnostics.reportInvalidNumber(location, text);
        }
    }

    private void readOctalNumber() {
        while ('0' <= current() && current() <= '7')
            currentPosition++;

        int length = currentPosition - currentTokenStart;
        String text = sourceText.substring(currentTokenStart, length);

        try {
            currentTokenValue = Integer.parseInt(text.substring(2), 8);
        } catch (NumberFormatException ignored) {
            TextSpan span = new TextSpan(currentTokenStart, length);
            TextLocation location = new TextLocation(sourceText, span);
            diagnostics.reportInvalidNumber(location, text);
        }
        currentTokenKind = SyntaxKind.NUMBER_TOKEN;
    }

    private void readBinaryNumber() {
        while (current() == '0' || current() == '1')
            currentPosition++;

        int length = currentPosition - currentTokenStart;
        String text = sourceText.substring(currentTokenStart, length);

        try {
            currentTokenValue = Integer.parseInt(text.substring(2), 2);
        } catch (NumberFormatException ignored) {
            TextSpan span = new TextSpan(currentTokenStart, length);
            TextLocation location = new TextLocation(sourceText, span);
            diagnostics.reportInvalidNumber(location, text);
        }
        currentTokenKind = SyntaxKind.NUMBER_TOKEN;
    }

    private void readHexNumber() {
        while (Character.isDigit(current()) || 'a' <= current() && current() <= 'f' || 'A' <= current() && current() <= 'F')
            currentPosition++;

        int length = currentPosition - currentTokenStart;
        String text = sourceText.substring(currentTokenStart, length);

        try {
            currentTokenValue = Integer.parseInt(text.substring(2), 16);
        } catch (NumberFormatException ignored) {
            TextSpan span = new TextSpan(currentTokenStart, length);
            TextLocation location = new TextLocation(sourceText, span);
            diagnostics.reportInvalidNumber(location, text);
        }
        currentTokenKind = SyntaxKind.NUMBER_TOKEN;
    }

    private char current() {
        return peek(0);
    }

    private char lookahead() {
        return peek(1);
    }

    private char peek(int offset) {
        int index = currentPosition + offset;
        return index >= sourceText.getLength() ? '\0' : sourceText.charAt(index);
    }
}
