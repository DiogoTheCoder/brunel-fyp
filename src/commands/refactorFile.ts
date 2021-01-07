import { getEditor, parse, readCode, writeCode } from '../utils';
import { LanguageClient } from 'vscode-languageclient';
import * as vscode from 'vscode';

export async function refactorFile(client: LanguageClient): Promise<void> {
  const activeTextEditor = getEditor();
  if (!activeTextEditor) {
    throw Error('No active text editor');
  }

  if (!activeTextEditor.document) {
    throw Error('No active Java file');
  }

  console.log(activeTextEditor.document.uri.fsPath);
  let transformedCode = await vscode.commands.executeCommand('mjga.langserver.refactorFile', activeTextEditor.document.uri.fsPath);
  if (typeof transformedCode === 'string') {
    writeCode(transformedCode);
  }
}
