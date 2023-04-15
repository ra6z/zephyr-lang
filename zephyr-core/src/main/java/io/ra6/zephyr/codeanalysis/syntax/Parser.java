package io.ra6.zephyr.codeanalysis.syntax;

import io.ra6.zephyr.codeanalysis.syntax.expressions.*;
import io.ra6.zephyr.codeanalysis.syntax.statements.*;
import io.ra6.zephyr.diagnostic.DiagnosticBag;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class Parser {
    private final SyntaxTree syntaxTree;
    private final List<SyntaxToken> tokens = new ArrayList<>();
    @Getter
    private final DiagnosticBag diagnostics = new DiagnosticBag();

    private int position;

    public Parser(SyntaxTree syntaxTree) {
        this.syntaxTree = syntaxTree;

        Lexer lexer = new Lexer(syntaxTree);
        SyntaxToken token;
        do {
            token = lexer.lex();

            if (token.getKind() == SyntaxKind.BAD_TOKEN) continue;
            if (token.getKind() == SyntaxKind.WHITESPACE_TOKEN) continue;

            tokens.add(token);

        } while (token.getKind() != SyntaxKind.END_OF_FILE_TOKEN);

        diagnostics.addAll(lexer.getDiagnostics());
    }

    public CompilationUnitSyntax parseCompilationUnit() {
        List<StatementSyntax> statements = new ArrayList<>();

        while (current().getKind() != SyntaxKind.END_OF_FILE_TOKEN) {
            StatementSyntax statement = parseCompilationUnitMember();
            statements.add(statement);
        }

        SyntaxToken eofToken = matchToken(SyntaxKind.END_OF_FILE_TOKEN);

        return new CompilationUnitSyntax(syntaxTree, statements, eofToken);
    }

    private StatementSyntax parseCompilationUnitMember() {
        return switch (current().getKind()) {
            case IMPORT_KEYWORD -> parseImportDeclaration();
            case TYPE_KEYWORD -> parseTypeDeclaration();
            case NATIVE_TYPE_KEYWORD -> parseNativeTypeDeclaration();
            case EXPORT_KEYWORD -> parseExportDeclaration();
            default -> {
                diagnostics.reportUnexpectedCompilationUnitMember(current().getLocation(), current().getKind(), new SyntaxKind[]{SyntaxKind.IMPORT_KEYWORD, SyntaxKind.TYPE_KEYWORD, SyntaxKind.EXPORT_KEYWORD});
                yield parseExpressionStatement();
            }
        };
    }

    private StatementSyntax parseNativeTypeDeclaration() {
        SyntaxToken nativeTypeKeyword = matchToken(SyntaxKind.NATIVE_TYPE_KEYWORD);
        SyntaxToken identifierToken = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
        SyntaxToken semicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);

        return new NativeTypeDeclarationSyntax(syntaxTree, nativeTypeKeyword, identifierToken, semicolonToken);
    }

    private StatementSyntax parseTypeDeclaration() {
        SyntaxToken typeKeyword = matchToken(SyntaxKind.TYPE_KEYWORD);
        SyntaxToken identifierToken = matchToken(SyntaxKind.IDENTIFIER_TOKEN);

        GenericParameterClauseSyntax genericParameterClause = null;
        if (current().getKind() == SyntaxKind.LESS_TOKEN) {
            genericParameterClause = parseGenericParameterClause();
        }

        SyntaxToken openBraceToken = matchToken(SyntaxKind.OPEN_BRACE_TOKEN);

        List<StatementSyntax> members = new ArrayList<>();
        while (current().getKind() != SyntaxKind.CLOSE_BRACE_TOKEN &&
                current().getKind() != SyntaxKind.END_OF_FILE_TOKEN) {
            SyntaxToken memberKeyword = current();

            StatementSyntax member = parseTypeMember();
            members.add(member);

            if (memberKeyword == current())
                break;
        }

        SyntaxToken closeBraceToken = matchToken(SyntaxKind.CLOSE_BRACE_TOKEN);
        return new TypeDeclarationSyntax(syntaxTree, typeKeyword, identifierToken, genericParameterClause, openBraceToken, members, closeBraceToken);
    }

    private GenericParameterClauseSyntax parseGenericParameterClause() {
        SyntaxToken lessToken = matchToken(SyntaxKind.LESS_TOKEN);

        List<SyntaxNode> parameters = new ArrayList<>();

        if (current().getKind() != SyntaxKind.GREATER_TOKEN) {
            GenericParameterSyntax firstParameter = parseGenericParameter();
            parameters.add(firstParameter);

            while (current().getKind() == SyntaxKind.COMMA_TOKEN) {
                SyntaxToken commaToken = matchToken(SyntaxKind.COMMA_TOKEN);
                GenericParameterSyntax nextParameter = parseGenericParameter();

                parameters.add(commaToken);
                parameters.add(nextParameter);
            }
        }

        SyntaxToken greaterToken = matchToken(SyntaxKind.GREATER_TOKEN);
        return new GenericParameterClauseSyntax(syntaxTree, lessToken, new SeparatedSyntaxList<>(parameters), greaterToken);
    }

    private GenericParameterSyntax parseGenericParameter() {
        SyntaxToken identifier = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
        return new GenericParameterSyntax(syntaxTree, identifier);
    }

    private StatementSyntax parseTypeMember() {
        if (current().getKind() == SyntaxKind.CONSTRUCTOR_KEYWORD) {
            return parseTypeConstructorDeclaration();
        }

        if (current().getKind() == SyntaxKind.BINARY_OPERATOR_KEYWORD) {
            return parseTypeBinaryOperatorDeclaration();
        }

        if (current().getKind() == SyntaxKind.UNARY_OPERATOR_KEYWORD) {
            return parseTypeUnaryOperatorDeclaration();
        }

        boolean isPublic = current().getKind() == SyntaxKind.PUB_KEYWORD;
        SyntaxToken visibilityToken = null;
        if (isPublic)
            visibilityToken = matchToken(SyntaxKind.PUB_KEYWORD);
        else if (current().getKind() == SyntaxKind.PRV_KEYWORD)
            visibilityToken = matchToken(SyntaxKind.PRV_KEYWORD);

        boolean isShared = current().getKind() == SyntaxKind.SHARED_KEYWORD;
        SyntaxToken sharedToken = null;
        if (isShared)
            sharedToken = matchToken(SyntaxKind.SHARED_KEYWORD);

        if (current().getKind() == SyntaxKind.CONST_KEYWORD ||
                current().getKind() == SyntaxKind.VAR_KEYWORD) {
            return parseTypeVariableDeclaration(visibilityToken, sharedToken);
        }

        if (current().getKind() == SyntaxKind.FNC_KEYWORD) {
            return parseTypeFunctionDeclaration(visibilityToken, sharedToken);
        }

        diagnostics.reportUnexpectedTypeMember(current().getLocation(), current().getKind(), new SyntaxKind[]{SyntaxKind.CONSTRUCTOR_KEYWORD, SyntaxKind.CONST_KEYWORD, SyntaxKind.VAR_KEYWORD, SyntaxKind.FNC_KEYWORD});
        return parseExpressionStatement();
    }

    private StatementSyntax parseTypeUnaryOperatorDeclaration() {
        SyntaxToken binaryOperatorKeyword = matchToken(SyntaxKind.UNARY_OPERATOR_KEYWORD);
        SyntaxToken operatorToken = nextToken();
        SyntaxToken openParenToken = matchToken(SyntaxKind.OPEN_PARENTHESIS_TOKEN);

        SyntaxToken closeParenToken = matchToken(SyntaxKind.CLOSE_PARENTHESIS_TOKEN);
        TypeClauseSyntax returnType = parseTypeClause();
        BlockStatementSyntax body = parseBlockStatement();

        return new TypeUnaryOperatorDeclarationSyntax(syntaxTree, binaryOperatorKeyword, operatorToken, openParenToken, closeParenToken, returnType, body);
    }

    private StatementSyntax parseTypeBinaryOperatorDeclaration() {
        SyntaxToken binaryOperatorKeyword = matchToken(SyntaxKind.BINARY_OPERATOR_KEYWORD);
        SyntaxToken operatorToken = nextToken();
        SyntaxToken openParenToken = matchToken(SyntaxKind.OPEN_PARENTHESIS_TOKEN);
        SyntaxToken rightOperandToken = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
        TypeClauseSyntax rightOperandType = parseTypeClause();
        SyntaxToken closeParenToken = matchToken(SyntaxKind.CLOSE_PARENTHESIS_TOKEN);
        TypeClauseSyntax returnType = parseTypeClause();
        BlockStatementSyntax body = parseBlockStatement();

        return new TypeBinaryOperatorDeclarationSyntax(syntaxTree, binaryOperatorKeyword, operatorToken, openParenToken, rightOperandToken, rightOperandType, closeParenToken, returnType, body);
    }

    private TypeConstructorDeclarationSyntax parseTypeConstructorDeclaration() {
        SyntaxToken constructorKeyword = matchToken(SyntaxKind.CONSTRUCTOR_KEYWORD);
        SyntaxToken openParenToken = matchToken(SyntaxKind.OPEN_PARENTHESIS_TOKEN);
        SeparatedSyntaxList<ParameterSyntax> parameters = parseParameterList();
        SyntaxToken closeParenToken = matchToken(SyntaxKind.CLOSE_PARENTHESIS_TOKEN);
        BlockStatementSyntax body = parseBlockStatement();
        return new TypeConstructorDeclarationSyntax(syntaxTree, constructorKeyword, openParenToken, parameters, closeParenToken, body);
    }

    private TypeFunctionDeclarationSyntax parseTypeFunctionDeclaration(SyntaxToken visibilityToken, SyntaxToken sharedToken) {
        SyntaxToken fncKeywordToken = matchToken(SyntaxKind.FNC_KEYWORD);
        SyntaxToken identifierToken = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
        SyntaxToken openParenToken = matchToken(SyntaxKind.OPEN_PARENTHESIS_TOKEN);
        SeparatedSyntaxList<ParameterSyntax> parameters = parseParameterList();
        SyntaxToken closeParenToken = matchToken(SyntaxKind.CLOSE_PARENTHESIS_TOKEN);
        TypeClauseSyntax typeClause = parseTypeClause();
        BlockStatementSyntax body = parseBlockStatement();

        return new TypeFunctionDeclarationSyntax(syntaxTree, visibilityToken, sharedToken, fncKeywordToken, identifierToken, openParenToken, parameters, closeParenToken, typeClause, body);
    }

    private TypeFieldDeclarationSyntax parseTypeVariableDeclaration(SyntaxToken visibilityToken, SyntaxToken sharedToken) {
        boolean isReadonly = current().getKind() == SyntaxKind.CONST_KEYWORD;
        SyntaxToken keywordToken = matchToken(isReadonly ? SyntaxKind.CONST_KEYWORD : SyntaxKind.VAR_KEYWORD);
        SyntaxToken identifierToken = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
        TypeClauseSyntax typeClause = parseTypeClause();

        SyntaxToken equalsToken = null;
        ExpressionSyntax initializer = null;
        SyntaxToken semicolonToken = null;

        if (isReadonly) {
            equalsToken = matchToken(SyntaxKind.EQUALS_TOKEN);
            initializer = parseExpression();
        } else {
            if (current().getKind() == SyntaxKind.EQUALS_TOKEN) {
                equalsToken = matchToken(SyntaxKind.EQUALS_TOKEN);
                initializer = parseExpression();
            }
        }
        semicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);

        return new TypeFieldDeclarationSyntax(syntaxTree, visibilityToken, sharedToken, keywordToken, identifierToken, typeClause, equalsToken, initializer, semicolonToken);
    }

    private BlockStatementSyntax parseBlockStatement() {
        SyntaxToken openBraceToken = matchToken(SyntaxKind.OPEN_BRACE_TOKEN);
        List<StatementSyntax> statements = new ArrayList<>();

        while (current().getKind() != SyntaxKind.CLOSE_BRACE_TOKEN) {
            SyntaxToken startToken = current();

            StatementSyntax statement = parseStatement();
            statements.add(statement);

            if (current() == startToken)
                nextToken();
        }

        SyntaxToken closeBraceToken = matchToken(SyntaxKind.CLOSE_BRACE_TOKEN);
        return new BlockStatementSyntax(syntaxTree, openBraceToken, statements, closeBraceToken);
    }

    private StatementSyntax parseStatement() {
        return switch (current().getKind()) {
            case VAR_KEYWORD, CONST_KEYWORD -> parseVariableDeclaration();
            case OPEN_BRACE_TOKEN -> parseBlockStatement();
            case IF_KEYWORD -> parseIfStatement();
            case WHILE_KEYWORD -> parseWhileStatement();
            case DO_KEYWORD -> parseDoWhileStatement();
            case FOR_KEYWORD -> parseForStatement();
            case BREAK_KEYWORD -> parseBreakStatement();
            case CONTINUE_KEYWORD -> parseContinueStatement();
            case RETURN_KEYWORD -> parseReturnStatement();

            default -> parseExpressionStatement();
        };
    }

    private StatementSyntax parseDoWhileStatement() {
        SyntaxToken doKeyword = matchToken(SyntaxKind.DO_KEYWORD);
        StatementSyntax body = parseStatement();
        SyntaxToken whileKeyword = matchToken(SyntaxKind.WHILE_KEYWORD);
        SyntaxToken openParenToken = matchToken(SyntaxKind.OPEN_PARENTHESIS_TOKEN);
        ExpressionSyntax condition = parseExpression();
        SyntaxToken closeParenToken = matchToken(SyntaxKind.CLOSE_PARENTHESIS_TOKEN);
        SyntaxToken semicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);
        return new DoWhileStatementSyntax(syntaxTree, doKeyword, body, whileKeyword, openParenToken, condition, closeParenToken, semicolonToken);
    }

    private StatementSyntax parseReturnStatement() {
        SyntaxToken returnKeyword = matchToken(SyntaxKind.RETURN_KEYWORD);
        ExpressionSyntax expression = null;
        if (current().getKind() != SyntaxKind.SEMICOLON_TOKEN) expression = parseExpression();
        SyntaxToken semicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);
        return new ReturnStatementSyntax(syntaxTree, returnKeyword, expression, semicolonToken);
    }

    private StatementSyntax parseContinueStatement() {
        SyntaxToken continueKeyword = matchToken(SyntaxKind.CONTINUE_KEYWORD);
        SyntaxToken semicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);
        return new ContinueStatementSyntax(syntaxTree, continueKeyword, semicolonToken);
    }

    private StatementSyntax parseBreakStatement() {
        SyntaxToken breakKeyword = matchToken(SyntaxKind.BREAK_KEYWORD);
        SyntaxToken semicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);
        return new BreakStatementSyntax(syntaxTree, breakKeyword, semicolonToken);
    }

    private StatementSyntax parseForStatement() {
        SyntaxToken forKeyword = matchToken(SyntaxKind.FOR_KEYWORD);
        SyntaxToken openParenToken = matchToken(SyntaxKind.OPEN_PARENTHESIS_TOKEN);

        ForInitializerClauseSyntax initializer = null;
        if (current().getKind() != SyntaxKind.SEMICOLON_TOKEN) initializer = parseForInitializerClause();
        SyntaxToken initializerSemicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);

        ExpressionSyntax condition = null;
        if (current().getKind() != SyntaxKind.SEMICOLON_TOKEN) condition = parseExpression();
        SyntaxToken conditionSemicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);

        ExpressionSyntax incrementer = null;
        if (current().getKind() != SyntaxKind.CLOSE_PARENTHESIS_TOKEN) incrementer = parseExpression();

        SyntaxToken closeParenToken = matchToken(SyntaxKind.CLOSE_PARENTHESIS_TOKEN);
        StatementSyntax body = parseStatement();

        return new ForStatementSyntax(syntaxTree, forKeyword, openParenToken, initializer, initializerSemicolonToken, condition, conditionSemicolonToken, incrementer, closeParenToken, body);
    }

    // TODO: add support for multiple initializers
    private ForInitializerClauseSyntax parseForInitializerClause() {
        SyntaxToken varKeyword = matchToken(SyntaxKind.VAR_KEYWORD);
        TypeClauseSyntax typeClause = parseTypeClause();

        SyntaxToken identifierToken = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
        SyntaxToken equalsToken = matchToken(SyntaxKind.EQUALS_TOKEN);
        ExpressionSyntax initializer = parseExpression();

        return new ForInitializerClauseSyntax(syntaxTree, varKeyword, typeClause, identifierToken, equalsToken, initializer);
    }

    private StatementSyntax parseWhileStatement() {
        SyntaxToken whileKeyword = matchToken(SyntaxKind.WHILE_KEYWORD);
        SyntaxToken openParenToken = matchToken(SyntaxKind.OPEN_PARENTHESIS_TOKEN);
        ExpressionSyntax condition = parseExpression();
        SyntaxToken closeParenToken = matchToken(SyntaxKind.CLOSE_PARENTHESIS_TOKEN);
        StatementSyntax body = parseStatement();
        return new WhileStatementSyntax(syntaxTree, whileKeyword, openParenToken, condition, closeParenToken, body);
    }

    private StatementSyntax parseIfStatement() {
        SyntaxToken ifKeyword = matchToken(SyntaxKind.IF_KEYWORD);
        SyntaxToken openParenToken = matchToken(SyntaxKind.OPEN_PARENTHESIS_TOKEN);
        ExpressionSyntax condition = parseExpression();
        SyntaxToken closeParenToken = matchToken(SyntaxKind.CLOSE_PARENTHESIS_TOKEN);
        StatementSyntax thenStatement = parseStatement();
        ElseClauseSyntax elseClause = null;
        if (current().getKind() == SyntaxKind.ELSE_KEYWORD) {
            SyntaxToken elseKeyword = matchToken(SyntaxKind.ELSE_KEYWORD);
            StatementSyntax elseStatement = parseStatement();
            elseClause = new ElseClauseSyntax(syntaxTree, elseKeyword, elseStatement);
        }
        return new IfStatementSyntax(syntaxTree, ifKeyword, openParenToken, condition, closeParenToken, thenStatement, elseClause);
    }

    private StatementSyntax parseVariableDeclaration() {
        boolean isReadonly = current().getKind() == SyntaxKind.CONST_KEYWORD;
        SyntaxToken keywordToken = matchToken(isReadonly ? SyntaxKind.CONST_KEYWORD : SyntaxKind.VAR_KEYWORD);
        SyntaxToken identifierToken = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
        TypeClauseSyntax typeClause = parseTypeClause();
        SyntaxToken equalsToken = matchToken(SyntaxKind.EQUALS_TOKEN);
        ExpressionSyntax initializer = parseExpression();
        SyntaxToken semicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);
        return new VariableDeclarationSyntax(syntaxTree, keywordToken, identifierToken, typeClause, equalsToken, initializer, semicolonToken);
    }

    private SeparatedSyntaxList<ParameterSyntax> parseParameterList() {
        List<SyntaxNode> nodes = new ArrayList<>();

        if (current().getKind() != SyntaxKind.CLOSE_PARENTHESIS_TOKEN) {
            nodes.add(parseParameter());

            while (current().getKind() == SyntaxKind.COMMA_TOKEN) {
                SyntaxToken commaToken = matchToken(SyntaxKind.COMMA_TOKEN);
                nodes.add(commaToken);
                nodes.add(parseParameter());
            }
        }

        return new SeparatedSyntaxList<>(nodes);
    }

    private ParameterSyntax parseParameter() {
        SyntaxToken identifierToken = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
        TypeClauseSyntax typeClause = parseTypeClause();
        return new ParameterSyntax(syntaxTree, identifierToken, typeClause);
    }

    private TypeClauseSyntax parseTypeClause() {
        SyntaxToken colonToken = matchToken(SyntaxKind.COLON_TOKEN);
        QualifiedNameSyntax typeName = parseQualifiedName();

        GenericParameterClauseSyntax genericParameter = null;
        if (current().getKind() == SyntaxKind.LESS_TOKEN) {
            genericParameter = parseGenericParameterClause();
        }


        if (current().getKind() == SyntaxKind.OPEN_BRACKET_TOKEN) {
            return parseArrayType(colonToken, typeName);
        }

        return new TypeClauseSyntax(syntaxTree, colonToken, typeName, genericParameter);
    }

    private TypeClauseSyntax parseArrayType(SyntaxToken colonToken, QualifiedNameSyntax elementName) {
        // parse multiple array
        List<SyntaxToken> brackets = new ArrayList<>();

        while (current().getKind() == SyntaxKind.OPEN_BRACKET_TOKEN) {
            SyntaxToken openBracketToken = matchToken(SyntaxKind.OPEN_BRACKET_TOKEN);
            SyntaxToken closeBracketToken = matchToken(SyntaxKind.CLOSE_BRACKET_TOKEN);
            brackets.add(openBracketToken);
            brackets.add(closeBracketToken);
        }

        return new ArrayTypeClauseSyntax(syntaxTree, colonToken, elementName, brackets);
    }

    private QualifiedNameSyntax parseQualifiedName() {
        SyntaxToken identifier = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
        List<SyntaxNode> nodes = new ArrayList<>();

        nodes.add(identifier);

        while (current().getKind() == SyntaxKind.DOT_TOKEN) {
            SyntaxToken dotToken = matchToken(SyntaxKind.DOT_TOKEN);
            SyntaxToken qualifier = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
            nodes.add(dotToken);
            nodes.add(qualifier);
        }

        return new QualifiedNameSyntax(syntaxTree, new SeparatedSyntaxList<>(nodes));
    }

    private StatementSyntax parseExportDeclaration() {
        SyntaxToken exportKeyword = matchToken(SyntaxKind.EXPORT_KEYWORD);
        QualifiedNameSyntax name = parseQualifiedName();
        SyntaxToken semicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);
        return new ExportDeclarationSyntax(syntaxTree, exportKeyword, name, semicolonToken);
    }

    private StatementSyntax parseImportDeclaration() {
        SyntaxToken importKeyword = matchToken(SyntaxKind.IMPORT_KEYWORD);
        SyntaxToken stringToken = matchToken(SyntaxKind.STRING_TOKEN);

        if (current().getKind() == SyntaxKind.AS_KEYWORD) {
            SyntaxToken asKeyword = matchToken(SyntaxKind.AS_KEYWORD);
            SyntaxToken identifierToken = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
            SyntaxToken semicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);
            return new ImportDeclarationSyntax(syntaxTree, importKeyword, stringToken, asKeyword, identifierToken, semicolonToken);
        }

        SyntaxToken semicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);
        return new ImportDeclarationSyntax(syntaxTree, importKeyword, stringToken, null, null, semicolonToken);
    }

    private StatementSyntax parseExpressionStatement() {
        ExpressionSyntax expression = parseExpression();
        SyntaxToken semicolonToken = matchToken(SyntaxKind.SEMICOLON_TOKEN);
        return new ExpressionStatementSyntax(syntaxTree, expression, semicolonToken);
    }


    //Expression parsing recursive descent
    public ExpressionSyntax parseExpression() {
        return parseAssignmentExpression();
    }

    private ExpressionSyntax parseAssignmentExpression() {
        ExpressionSyntax left = parseConditionalExpression();

        if (current().getKind() == SyntaxKind.EQUALS_TOKEN) {
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseAssignmentExpression();
            return new AssignmentExpressionSyntax(syntaxTree, left, operatorToken, right);
        }

        if (current().getKind() == SyntaxKind.PLUS_EQUALS_TOKEN ||
                current().getKind() == SyntaxKind.MINUS_EQUALS_TOKEN ||
                current().getKind() == SyntaxKind.STAR_EQUALS_TOKEN ||
                current().getKind() == SyntaxKind.SLASH_EQUALS_TOKEN) {
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseAssignmentExpression();
            return new AssignmentExpressionSyntax(syntaxTree, left, operatorToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseConditionalExpression() {
        ExpressionSyntax left = parseLogicalOrExpression();

        if (current().getKind() == SyntaxKind.QUESTION_TOKEN) {
            SyntaxToken questionToken = nextToken();
            ExpressionSyntax middle = parseExpression();
            SyntaxToken colonToken = matchToken(SyntaxKind.COLON_TOKEN);
            ExpressionSyntax right = parseExpression();
            return new ConditionalExpressionSyntax(syntaxTree, left, questionToken, middle, colonToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseLogicalOrExpression() {
        ExpressionSyntax left = parseLogicalAndExpression();

        while (current().getKind() == SyntaxKind.PIPE_PIPE_TOKEN) {
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseLogicalAndExpression();
            left = new BinaryExpressionSyntax(syntaxTree, left, operatorToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseLogicalAndExpression() {
        ExpressionSyntax left = parseBitwiseOrExpression();

        while (current().getKind() == SyntaxKind.AMPERSAND_AMPERSAND_TOKEN) {
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseBitwiseOrExpression();
            left = new BinaryExpressionSyntax(syntaxTree, left, operatorToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseBitwiseOrExpression() {
        ExpressionSyntax left = parseBitwiseXorExpression();

        while (current().getKind() == SyntaxKind.PIPE_TOKEN) {
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseBitwiseXorExpression();
            left = new BinaryExpressionSyntax(syntaxTree, left, operatorToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseBitwiseXorExpression() {
        ExpressionSyntax left = parseBitwiseAndExpression();

        while (current().getKind() == SyntaxKind.CARET_TOKEN) {
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseBitwiseAndExpression();
            left = new BinaryExpressionSyntax(syntaxTree, left, operatorToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseBitwiseAndExpression() {
        ExpressionSyntax left = parseEqualityExpression();

        while (current().getKind() == SyntaxKind.AMPERSAND_TOKEN) {
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseEqualityExpression();
            left = new BinaryExpressionSyntax(syntaxTree, left, operatorToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseEqualityExpression() {
        ExpressionSyntax left = parseRelationalExpression();

        while (current().getKind() == SyntaxKind.BANG_EQUALS_TOKEN ||
                current().getKind() == SyntaxKind.EQUALS_EQUALS_TOKEN) {

            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseRelationalExpression();
            left = new BinaryExpressionSyntax(syntaxTree, left, operatorToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseRelationalExpression() {
        ExpressionSyntax left = parseShiftExpression();

        while (current().getKind() == SyntaxKind.LESS_TOKEN ||
                current().getKind() == SyntaxKind.LESS_EQUALS_TOKEN ||
                current().getKind() == SyntaxKind.GREATER_TOKEN ||
                current().getKind() == SyntaxKind.GREATER_EQUALS_TOKEN) {

            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseShiftExpression();
            left = new BinaryExpressionSyntax(syntaxTree, left, operatorToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseShiftExpression() {
        ExpressionSyntax left = parseAdditiveExpression();

        while (current().getKind() == SyntaxKind.LESS_LESS_TOKEN ||
                current().getKind() == SyntaxKind.GREATER_GREATER_TOKEN) {

            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseAdditiveExpression();
            left = new BinaryExpressionSyntax(syntaxTree, left, operatorToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseAdditiveExpression() {
        ExpressionSyntax left = parseMultiplicativeExpression();

        while (current().getKind() == SyntaxKind.PLUS_TOKEN ||
                current().getKind() == SyntaxKind.MINUS_TOKEN) {

            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseMultiplicativeExpression();
            left = new BinaryExpressionSyntax(syntaxTree, left, operatorToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseMultiplicativeExpression() {
        ExpressionSyntax left = parseUnaryExpression();

        while (current().getKind() == SyntaxKind.STAR_TOKEN ||
                current().getKind() == SyntaxKind.SLASH_TOKEN ||
                current().getKind() == SyntaxKind.PERCENT_TOKEN) {

            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseUnaryExpression();
            left = new BinaryExpressionSyntax(syntaxTree, left, operatorToken, right);
        }

        return left;
    }

    private ExpressionSyntax parseUnaryExpression() {
        if (current().getKind() == SyntaxKind.BANG_TOKEN ||
                current().getKind() == SyntaxKind.PLUS_TOKEN ||
                current().getKind() == SyntaxKind.MINUS_TOKEN ||
                current().getKind() == SyntaxKind.TILDE_TOKEN) {

            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax operand = parseUnaryExpression();
            return new UnaryExpressionSyntax(syntaxTree, operatorToken, operand);
        }

        if (current().getKind() == SyntaxKind.NEW_KEYWORD) {
            SyntaxToken newKeyword = matchToken(SyntaxKind.NEW_KEYWORD);
            QualifiedNameSyntax qualifiedName = parseQualifiedName();

            GenericParameterClauseSyntax genericParameter = null;
            if (current().getKind() == SyntaxKind.LESS_TOKEN) {
                genericParameter = parseGenericParameterClause();
            }

            if (current().getKind() == SyntaxKind.OPEN_BRACKET_TOKEN) {
                List<ArraySizeClauseSyntax> arraySizeClauses = new ArrayList<>();
                ArraySizeClauseSyntax arraySizeClause = parseArraySizeClause();
                arraySizeClauses.add(arraySizeClause);

                while (current().getKind() == SyntaxKind.OPEN_BRACKET_TOKEN) {
                    arraySizeClause = parseArraySizeClause();
                    arraySizeClauses.add(arraySizeClause);
                }

                // parse array creation expression
                return new ArrayCreationExpressionSyntax(syntaxTree, newKeyword, qualifiedName, genericParameter, arraySizeClauses);
            }

            SyntaxToken leftParenthesis = nextToken();
            SeparatedSyntaxList<ExpressionSyntax> arguments = parseArguments();
            SyntaxToken rightParenthesis = matchToken(SyntaxKind.CLOSE_PARENTHESIS_TOKEN);
            return new InstanceCreationExpressionSyntax(syntaxTree, newKeyword, genericParameter, qualifiedName, leftParenthesis, arguments, rightParenthesis);
        }

        return parsePrimaryExpression();
    }

    private ArraySizeClauseSyntax parseArraySizeClause() {
        SyntaxToken openBracket = matchToken(SyntaxKind.OPEN_BRACKET_TOKEN);
        ExpressionSyntax size = parseExpression();
        SyntaxToken closeBracket = matchToken(SyntaxKind.CLOSE_BRACKET_TOKEN);
        return new ArraySizeClauseSyntax(syntaxTree, openBracket, size, closeBracket);
    }

    private SeparatedSyntaxList<ExpressionSyntax> parseArrayElements() {
        List<SyntaxNode> nodesAndSeparators = new ArrayList<>();

        if (current().getKind() != SyntaxKind.CLOSE_BRACKET_TOKEN) {
            ExpressionSyntax firstArgument = parseExpression();
            nodesAndSeparators.add(firstArgument);
            while (current().getKind() == SyntaxKind.COMMA_TOKEN) {
                SyntaxToken comma = nextToken();
                ExpressionSyntax element = parseExpression();
                nodesAndSeparators.add(comma);
                nodesAndSeparators.add(element);
            }
        }

        return new SeparatedSyntaxList<>(nodesAndSeparators);
    }

    private ExpressionSyntax parsePrimaryExpression() {
        ExpressionSyntax expression = parseInternalPrimary();

        while (true) {
            if (current().getKind() == SyntaxKind.DOT_TOKEN) {
                SyntaxToken dotToken = nextToken();
                SyntaxToken memberToken = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
                expression = new MemberAccessExpressionSyntax(syntaxTree, expression, dotToken, memberToken);
            } else if (current().getKind() == SyntaxKind.OPEN_BRACKET_TOKEN) {
                SyntaxToken leftBracket = nextToken();
                ExpressionSyntax index = parseExpression();
                SyntaxToken rightBracket = matchToken(SyntaxKind.CLOSE_BRACKET_TOKEN);
                expression = new ArrayAccessExpressionSyntax(syntaxTree, expression, leftBracket, index, rightBracket);
            } else {
                break;
            }
        }

        if (current().getKind() == SyntaxKind.OPEN_PARENTHESIS_TOKEN) {
            SyntaxToken leftParenthesis = nextToken();
            SeparatedSyntaxList<ExpressionSyntax> arguments = parseArguments();
            SyntaxToken rightParenthesis = matchToken(SyntaxKind.CLOSE_PARENTHESIS_TOKEN);
            return new FunctionCallExpressionSyntax(syntaxTree, expression, leftParenthesis, arguments, rightParenthesis);
        }

        return expression;
    }

    private ExpressionSyntax parseInternalPrimary() {
        // parse parenthesized expression
        if (current().getKind() == SyntaxKind.OPEN_PARENTHESIS_TOKEN) {
            SyntaxToken left = nextToken();
            ExpressionSyntax expression = parseExpression();
            SyntaxToken right = matchToken(SyntaxKind.CLOSE_PARENTHESIS_TOKEN);
            return new ParenthesizedExpressionSyntax(syntaxTree, left, expression, right);
        }

        // parse number literal
        if (current().getKind() == SyntaxKind.NUMBER_TOKEN) {
            SyntaxToken numberToken = nextToken();
            return new LiteralExpressionSyntax(syntaxTree, numberToken);
        }

        if (current().getKind() == SyntaxKind.FLOATING_POINT_TOKEN) {
            SyntaxToken floatingPointToken = nextToken();
            return new LiteralExpressionSyntax(syntaxTree, floatingPointToken);
        }

        // parse boolean literal
        if (current().getKind() == SyntaxKind.TRUE_KEYWORD ||
                current().getKind() == SyntaxKind.FALSE_KEYWORD) {
            SyntaxToken booleanToken = nextToken();
            return new LiteralExpressionSyntax(syntaxTree, booleanToken);
        }

        // parse string literal
        if (current().getKind() == SyntaxKind.STRING_TOKEN) {
            SyntaxToken stringToken = nextToken();
            return new LiteralExpressionSyntax(syntaxTree, stringToken);
        }

        // parse array creation
        if (current().getKind() == SyntaxKind.OPEN_BRACKET_TOKEN) {
            SyntaxToken leftBracket = nextToken();
            SeparatedSyntaxList<ExpressionSyntax> elements = parseArrayElements();
            SyntaxToken rightBracket = matchToken(SyntaxKind.CLOSE_BRACKET_TOKEN);
            return new ArrayLiteralExpressionSyntax(syntaxTree, leftBracket, elements, rightBracket);
        }

        SyntaxToken identifier = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
        ExpressionSyntax expression = new NameExpressionSyntax(syntaxTree, identifier);

        if (current().getKind() == SyntaxKind.OPEN_BRACKET_TOKEN) {
            SyntaxToken leftBracket = nextToken();
            ExpressionSyntax index = parseExpression();
            SyntaxToken rightBracket = matchToken(SyntaxKind.CLOSE_BRACKET_TOKEN);
            expression = new ArrayAccessExpressionSyntax(syntaxTree, expression, leftBracket, index, rightBracket);
        }
        while (current().getKind() == SyntaxKind.DOT_TOKEN || current().getKind() == SyntaxKind.OPEN_BRACKET_TOKEN) {
            if (current().getKind() == SyntaxKind.DOT_TOKEN) {
                SyntaxToken dotToken = nextToken();
                SyntaxToken memberToken = matchToken(SyntaxKind.IDENTIFIER_TOKEN);
                expression = new MemberAccessExpressionSyntax(syntaxTree, expression, dotToken, memberToken);
            } else {
                SyntaxToken openBracketToken = matchToken(SyntaxKind.OPEN_BRACKET_TOKEN);
                ExpressionSyntax index = parseExpression();
                SyntaxToken closeBracketToken = matchToken(SyntaxKind.CLOSE_BRACKET_TOKEN);
                expression = new ArrayAccessExpressionSyntax(syntaxTree, expression, openBracketToken, index, closeBracketToken);
            }
        }

        if (current().getKind() == SyntaxKind.OPEN_PARENTHESIS_TOKEN) {
            SyntaxToken leftParenthesis = nextToken();
            SeparatedSyntaxList<ExpressionSyntax> arguments = parseArguments();
            SyntaxToken rightParenthesis = matchToken(SyntaxKind.CLOSE_PARENTHESIS_TOKEN);
            return new FunctionCallExpressionSyntax(syntaxTree, expression, leftParenthesis, arguments, rightParenthesis);
        }

        return expression;
    }

    private SeparatedSyntaxList<ExpressionSyntax> parseArguments() {
        List<SyntaxNode> nodesAndSeparators = new ArrayList<>();
        if (current().getKind() != SyntaxKind.CLOSE_PARENTHESIS_TOKEN) {
            ExpressionSyntax firstArgument = parseExpression();
            nodesAndSeparators.add(firstArgument);
            while (current().getKind() == SyntaxKind.COMMA_TOKEN) {
                SyntaxToken comma = nextToken();
                ExpressionSyntax argument = parseExpression();
                nodesAndSeparators.add(comma);
                nodesAndSeparators.add(argument);
            }
        }
        return new SeparatedSyntaxList<>(nodesAndSeparators);
    }

    private SyntaxToken matchToken(SyntaxKind kind) {
        if (current().getKind() == kind)
            return nextToken();

        diagnostics.reportUnexpectedToken(current().getLocation(), current().getKind(), kind);
        return new SyntaxToken(syntaxTree, kind, current().getSpan(), null, null);
    }

    private SyntaxToken current() {
        return peek(0);
    }

    private SyntaxToken lookahead() {
        return peek(1);
    }

    private SyntaxToken nextToken() {
        SyntaxToken current = current();
        position++;
        return current;
    }

    private SyntaxToken peek(int offset) {
        int index = position + offset;
        if (index >= tokens.size())
            return tokens.get(tokens.size() - 1);

        return tokens.get(index);
    }
}
