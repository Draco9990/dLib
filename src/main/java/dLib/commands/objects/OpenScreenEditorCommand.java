package dLib.commands.objects;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.ui.screens.ScreenManager;

public class OpenScreenEditorCommand extends ConsoleCommand {
    public OpenScreenEditorCommand() {
        this.requiresPlayer = false;
        this.maxExtraTokens = 0;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        ScreenManager.openScreen(new ScreenEditorBaseScreen());
    }

    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
    }
}