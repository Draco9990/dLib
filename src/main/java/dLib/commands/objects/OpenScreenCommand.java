package dLib.commands.objects;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;

public class OpenScreenCommand extends ConsoleCommand {
    public OpenScreenCommand() {
        this.requiresPlayer = false;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
    }

    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
    }
}