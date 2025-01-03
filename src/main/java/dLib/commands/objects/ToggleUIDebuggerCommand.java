package dLib.commands.objects;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import dLib.tools.uidebugger.ui.UIDebuggerScreen;

public class ToggleUIDebuggerCommand extends ConsoleCommand {
    private UIDebuggerScreen screenCache;

    public ToggleUIDebuggerCommand() {
        this.requiresPlayer = false;
        this.maxExtraTokens = 0;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        if(screenCache == null) {
            screenCache = new UIDebuggerScreen();
            screenCache.open();
        }
        else{
            screenCache.close();
            screenCache = null;
        }
    }

    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
    }
}