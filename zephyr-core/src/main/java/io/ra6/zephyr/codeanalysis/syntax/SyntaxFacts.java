package io.ra6.zephyr.codeanalysis.syntax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SyntaxFacts {

    private static final HashMap<SyntaxKind, String> KEYWORDS = new HashMap<>() {
        {
            put(SyntaxKind.TRUE_KEYWORD, "true");
            put(SyntaxKind.FALSE_KEYWORD, "false");

            put(SyntaxKind.VAR_KEYWORD, "var");
            put(SyntaxKind.CONST_KEYWORD, "const");
            put(SyntaxKind.FNC_KEYWORD, "fnc");
            put(SyntaxKind.RETURN_KEYWORD, "return");

            put(SyntaxKind.IMPORT_KEYWORD, "import");
            put(SyntaxKind.AS_KEYWORD, "as");
            put(SyntaxKind.EXPORT_KEYWORD, "export");

            put(SyntaxKind.IF_KEYWORD, "if");
            put(SyntaxKind.ELSE_KEYWORD, "else");

            put(SyntaxKind.WHILE_KEYWORD, "while");
            put(SyntaxKind.DO_KEYWORD, "do");
            put(SyntaxKind.FOR_KEYWORD, "for");
            put(SyntaxKind.BREAK_KEYWORD, "break");
            put(SyntaxKind.CONTINUE_KEYWORD, "continue");

            put(SyntaxKind.PUB_KEYWORD, "pub");
            put(SyntaxKind.PRV_KEYWORD, "prv");
            put(SyntaxKind.SHARED_KEYWORD, "shared");

            put(SyntaxKind.TYPE_KEYWORD, "type");
            put(SyntaxKind.NATIVE_TYPE_KEYWORD, "nativetype");
            put(SyntaxKind.NEW_KEYWORD, "new");
            put(SyntaxKind.CONSTRUCTOR_KEYWORD, "constructor");

            put(SyntaxKind.UNARY_OPERATOR_KEYWORD, "unop");
            put(SyntaxKind.BINARY_OPERATOR_KEYWORD, "binop");
            put(SyntaxKind.IS_KEYWORD, "is");
        }
    };

    private static final List<SyntaxKind> BINARY_OPERATORS = List.of(
            SyntaxKind.PLUS_TOKEN,
            SyntaxKind.MINUS_TOKEN,
            SyntaxKind.SLASH_TOKEN,
            SyntaxKind.STAR_TOKEN,
            SyntaxKind.PERCENT_TOKEN,
            SyntaxKind.EQUALS_EQUALS_TOKEN,
            SyntaxKind.BANG_EQUALS_TOKEN,
            SyntaxKind.LESS_TOKEN,
            SyntaxKind.LESS_EQUALS_TOKEN,
            SyntaxKind.GREATER_TOKEN,
            SyntaxKind.GREATER_EQUALS_TOKEN,
            SyntaxKind.AMPERSAND_AMPERSAND_TOKEN,
            SyntaxKind.PIPE_PIPE_TOKEN
    );

    private static final List<SyntaxKind> UNARY_OPERATORS = List.of(
            SyntaxKind.BANG_TOKEN,
            SyntaxKind.MINUS_TOKEN,
            SyntaxKind.PLUS_TOKEN,
            SyntaxKind.TILDE_TOKEN
    );

    public static boolean isBinaryOperator(SyntaxKind kind) {
        return BINARY_OPERATORS.contains(kind);
    }

    public static boolean isUnaryOperator(SyntaxKind kind) {
        return UNARY_OPERATORS.contains(kind);
    }

    public static String getText(SyntaxKind kind) {
        if (KEYWORDS.containsKey(kind)) {
            return KEYWORDS.get(kind);
        }

        return switch (kind) {
            case PLUS_TOKEN -> "+";
            case MINUS_TOKEN -> "-";
            case SLASH_TOKEN -> "/";
            case STAR_TOKEN -> "*";
            case PERCENT_TOKEN -> "%";
            case EQUALS_TOKEN -> "=";
            case COLON_TOKEN -> ":";
            case SEMICOLON_TOKEN -> ";";
            case COMMA_TOKEN -> ",";
            case QUESTION_TOKEN -> "?";
            case DOT_TOKEN -> ".";
            case PIPE_TOKEN -> "|";
            case AMPERSAND_TOKEN -> "&";
            case CARET_TOKEN -> "^";
            case BANG_TOKEN -> "!";
            case TILDE_TOKEN -> "~";

            case BANG_EQUALS_TOKEN -> "!=";
            case PLUS_PLUS_TOKEN -> "++";
            case MINUS_MINUS_TOKEN -> "--";
            case EQUALS_EQUALS_TOKEN -> "==";
            case PLUS_EQUALS_TOKEN -> "+=";
            case MINUS_EQUALS_TOKEN -> "-=";
            case SLASH_EQUALS_TOKEN -> "/=";
            case STAR_EQUALS_TOKEN -> "*=";
            case PIPE_PIPE_TOKEN -> "||";
            case AMPERSAND_AMPERSAND_TOKEN -> "&&";
            case LESS_TOKEN -> "<";
            case LESS_EQUALS_TOKEN -> "<=";
            case GREATER_TOKEN -> ">";
            case GREATER_EQUALS_TOKEN -> ">=";

            case OPEN_PARENTHESIS_TOKEN -> "(";
            case CLOSE_PARENTHESIS_TOKEN -> ")";
            case OPEN_BRACKET_TOKEN -> "[";
            case CLOSE_BRACKET_TOKEN -> "]";
            case OPEN_BRACE_TOKEN -> "{";
            case CLOSE_BRACE_TOKEN -> "}";

            default -> null;
        };
    }

    public static SyntaxKind getKeywordKind(String tokenText) {
        return KEYWORDS.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(tokenText))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(SyntaxKind.IDENTIFIER_TOKEN);
    }
}
