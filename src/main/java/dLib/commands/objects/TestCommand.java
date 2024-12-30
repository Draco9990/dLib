package dLib.commands.objects;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;

public class TestCommand extends ConsoleCommand {
    public TestCommand() {
        this.requiresPlayer = false;
        this.maxExtraTokens = 0;
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