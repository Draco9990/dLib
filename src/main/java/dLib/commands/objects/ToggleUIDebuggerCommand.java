package dLib.commands.objects;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import dLib.tools.uidebugger.ui.UIDebuggerScreen;
import dLib.ui.UIManager;

public class ToggleUIDebuggerCommand extends ConsoleCommand {

    public ToggleUIDebuggerCommand() {
        this.requiresPlayer = false;
        this.maxExtraTokens = 0;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        if(UIManager.getOpenElementOfType(UIDebuggerScreen.class) == null) {
            new UIDebuggerScreen().open();
        }
        else{
            UIManager.getOpenElementOfType(UIDebuggerScreen.class).dispose();
        }
    }

    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
    }
}