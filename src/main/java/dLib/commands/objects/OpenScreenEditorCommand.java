package dLib.commands.objects;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import dLib.DLib;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.screens.ScreenManager;

public class OpenScreenEditorCommand extends ConsoleCommand {
    public OpenScreenEditorCommand() {
        this.requiresPlayer = false;
        this.maxExtraTokens = 0;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        //ScreenManager.openScreen(new ScreenEditorCanvasScreen());
    }

    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
    }
}