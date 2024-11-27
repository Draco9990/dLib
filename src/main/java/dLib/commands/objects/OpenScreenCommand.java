package dLib.commands.objects;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import dLib.DLib;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.screens.UIManager;

public class OpenScreenCommand extends ConsoleCommand {
    public OpenScreenCommand() {
        this.requiresPlayer = false;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        try{
            Class<? extends AbstractScreen> screen = (Class<? extends AbstractScreen>) Class.forName("dLib.ui.screens." + tokens[1]);
            AbstractScreen screenObject = screen.newInstance();
            screenObject.open();
            return;
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException ignored){}

        try{
            Class<? extends AbstractScreen> screen = (Class<? extends AbstractScreen>) Class.forName(tokens[1]);
            AbstractScreen screenObject = screen.newInstance();
            screenObject.open();
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException ignored){}

        DLib.logError("Could not find class with name " + tokens[1]);
    }

    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
    }
}