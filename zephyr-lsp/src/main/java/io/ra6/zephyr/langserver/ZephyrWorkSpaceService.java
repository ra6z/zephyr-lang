package io.ra6.zephyr.langserver;

import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.services.WorkspaceService;

/**
 * WorkspaceService implementation for Zephyr.
 */
public class ZephyrWorkSpaceService implements WorkspaceService {

    private ZephyrLanguageServer languageServer;

    public ZephyrWorkSpaceService(ZephyrLanguageServer languageServer) {
        this.languageServer = languageServer;
    }

    @Override
    public void didChangeConfiguration(DidChangeConfigurationParams didChangeConfigurationParams) {
    }

    @Override
    public void didChangeWatchedFiles(DidChangeWatchedFilesParams didChangeWatchedFilesParams) {
    }


}