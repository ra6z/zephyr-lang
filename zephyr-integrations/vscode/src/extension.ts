
// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import path = require("path");
import * as vscode from "vscode";

import { lspExtensionInstance } from "./lspcore";


export function activate(context: vscode.ExtensionContext) {
    return;

    //Set the context of the extension instance
    lspExtensionInstance.setContext(context);
    //Initialize the LS Client extension instance.
    lspExtensionInstance.init().catch((error) => {
        console.log("Failed to activate Zephyr extension. " + (error));
    })
}

export function deactivate() { }