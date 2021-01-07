package org.brunel.fyp.langserver;

import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.MessageType;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.WorkspaceSymbolParams;
import org.eclipse.lsp4j.services.WorkspaceService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;

public class MJGAWorkspaceService implements WorkspaceService {
    @Override
    public CompletableFuture<Object> executeCommand(ExecuteCommandParams params) {
        return CompletableFuture.supplyAsync(() -> {
            if (params.getCommand().equals("mjga.langserver.refactorFile")) {
                File file = new File(params.getArguments().get(0).toString());
                String filePath = file.getPath().replaceAll("\"", "");
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Parsing Java code from file: " + filePath);
                CompilationUnit compilationUnit;
                try {
                    compilationUnit = StaticJavaParser.parse(new FileInputStream(filePath));
                    compilationUnit.findAll(ForEachStmt.class)
                        .stream()
                        .forEach(forEachStmt -> {
                            forEachStmt.asForStmt();
                        });

                    return compilationUnit.toString();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                return null;
            }

            throw new UnsupportedOperationException();
        });
    }

    @Override
    public CompletableFuture<List<? extends SymbolInformation>> symbol(WorkspaceSymbolParams workspaceSymbolParams) {
        return null;
    }

    @Override
    public void didChangeConfiguration(DidChangeConfigurationParams didChangeConfigurationParams) {

    }

    @Override
    public void didChangeWatchedFiles(DidChangeWatchedFilesParams didChangeWatchedFilesParams) {

    }
}
