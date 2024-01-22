package dLib.plugin.intellij.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import dLib.DLib;
import dLib.plugin.intellij.PluginManager;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.screens.ScreenManager;

import java.util.ArrayList;
import java.util.Arrays;

public class IntelliJCommand extends ConsoleCommand {
    public IntelliJCommand() {
        this.requiresPlayer = false;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        if(tokens.length < 2) return;

        if(tokens[1].equals("enable")){
            PluginManager.enable();
            PluginManager.start();
        }
        else if(tokens[1].equals("disable")){
            PluginManager.shutdown();
            PluginManager.disable();
        }
    }

    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        return new ArrayList<>(Arrays.asList(
            (PluginManager.isEnabled() ? "disable" : "enable")
        ));
    }
}