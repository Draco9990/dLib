package dLib.plugin.intellij.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import dLib.plugin.intellij.PluginManager;

import java.util.ArrayList;
import java.util.Arrays;

public class TestCommand extends ConsoleCommand {
    public TestCommand() {
        this.requiresPlayer = false;
    }

    public void execute(String[] tokens, int depth) {
        PluginManager.sendMessage("createClass", "TemplateName");
    }

    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
    }
}