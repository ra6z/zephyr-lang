package io.ra6.zephyr.langserver;

import io.ra6.zephyr.codeanalysis.binding.Binder;
import io.ra6.zephyr.codeanalysis.binding.BoundProgram;
import io.ra6.zephyr.codeanalysis.syntax.SyntaxTree;
import io.ra6.zephyr.sourcefile.SourceText;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * TextDocumentService implementation for Zephyr.
 */
public class ZephyrTextDocumentService implements TextDocumentService {

    private ZephyrLanguageServer languageServer;

    private BoundProgram boundProgram;

    public ZephyrTextDocumentService(ZephyrLanguageServer languageServer) {
        this.languageServer = languageServer;
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams didOpenTextDocumentParams) {
        SourceText sourceText = SourceText.fromString(didOpenTextDocumentParams.getTextDocument().getText());
        SyntaxTree syntaxTree = SyntaxTree.parse(sourceText);
        Binder binder = new Binder(syntaxTree, languageServer.getStandardLibrary());

        boundProgram = binder.bindProgram();

        if (binder.getDiagnostics().hasErrors() || binder.getDiagnostics().hasWarnings()) {
            List<Diagnostic> diagnostics = new ArrayList<>();

            for (io.ra6.zephyr.diagnostic.Diagnostic diagnostic : binder.getDiagnostics().asList()) {
                Diagnostic lspDiagnostic = new Diagnostic();
                lspDiagnostic.setSeverity(diagnostic.isError() ? DiagnosticSeverity.Error : DiagnosticSeverity.Warning);
                lspDiagnostic.setMessage(diagnostic.getMessage());

                Position startPosition = new Position(
                        diagnostic.getLocation().getStartLine(),
                        diagnostic.getLocation().getSpan().getStart() + 1);

                Position endPosition = new Position(
                        diagnostic.getLocation().getEndLine(),
                        diagnostic.getLocation().getSpan().getEnd() + 1);

                lspDiagnostic.setRange(new Range(startPosition, endPosition));

                diagnostics.add(lspDiagnostic);
            }

            PublishDiagnosticsParams publishDiagnosticsParams = new PublishDiagnosticsParams(didOpenTextDocumentParams.getTextDocument().getUri(), diagnostics);
            languageServer.languageClient.publishDiagnostics(publishDiagnosticsParams);
        }
    }

    @Override
    public void didChange(DidChangeTextDocumentParams didChangeTextDocumentParams) {
        VersionedTextDocumentIdentifier documentIdentifier = didChangeTextDocumentParams.getTextDocument();
        String fileContent = didChangeTextDocumentParams.getContentChanges().get(0).getText();
        SourceText sourceText = SourceText.fromString(fileContent);
        SyntaxTree syntaxTree = SyntaxTree.parse(sourceText);
        Binder binder = new Binder(syntaxTree, languageServer.getStandardLibrary());

        boundProgram = binder.bindProgram();

        if (binder.getDiagnostics().hasErrors() || binder.getDiagnostics().hasWarnings()) {
            List<Diagnostic> diagnostics = new ArrayList<>();

            for (io.ra6.zephyr.diagnostic.Diagnostic diagnostic : binder.getDiagnostics().asList()) {
                Diagnostic lspDiagnostic = new Diagnostic();
                lspDiagnostic.setSeverity(diagnostic.isError() ? DiagnosticSeverity.Error : DiagnosticSeverity.Warning);
                lspDiagnostic.setMessage(diagnostic.getMessage());
                lspDiagnostic.setRange(new Range(new Position(
                        diagnostic.getLocation().getStartLine(),
                        diagnostic.getLocation().getSpan().getStart() + 1),
                        new Position(
                                diagnostic.getLocation().getEndLine(),
                                diagnostic.getLocation().getSpan().getEnd() + 1
                        )));
                diagnostics.add(lspDiagnostic);
            }

            PublishDiagnosticsParams publishDiagnosticsParams = new PublishDiagnosticsParams(didChangeTextDocumentParams.getTextDocument().getUri(), diagnostics);
            languageServer.languageClient.publishDiagnostics(publishDiagnosticsParams);
        }
    }


    @Override
    public void didClose(DidCloseTextDocumentParams didCloseTextDocumentParams) {
    }

    @Override
    public void didSave(DidSaveTextDocumentParams didSaveTextDocumentParams) {
    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
        return CompletableFuture.supplyAsync(() -> {
            CompletionItem completionItem = new CompletionItem();
            completionItem.setLabel("Test completion item");
            completionItem.setInsertText("Test");
            completionItem.setDetail("Snippet");
            completionItem.setDocumentation("This is a test completion item");
            completionItem.setKind(CompletionItemKind.Snippet);
            return Either.forLeft(List.of(completionItem));
        });
    }
}