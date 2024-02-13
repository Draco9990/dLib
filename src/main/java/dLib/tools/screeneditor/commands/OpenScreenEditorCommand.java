package dLib.tools.screeneditor.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.util.ScreenEditorSaveManager;
import dLib.ui.data.AbstractScreenData;
import dLib.ui.screens.ScreenManager;

import java.util.Objects;

public class OpenScreenEditorCommand extends ConsoleCommand {
    public OpenScreenEditorCommand() {
        this.requiresPlayer = false;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        if(tokens.length <= 1){
            ScreenManager.openScreen(new ScreenEditorBaseScreen());
        }
        else if(Objects.equals(tokens[1], "load")){
            ScreenManager.openScreen(ScreenEditorSaveManager.load());
        }
        else if(Objects.equals(tokens[1], "screen")){
            AbstractScreenData data = AbstractScreenData.deserialize("D:/savedata.dscreen");
            ScreenManager.openScreen(data.makeLiveInstance());
        }
    }

    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
    }
}