package dLib.commands;

import basemod.devcommands.ConsoleCommand;
import dLib.developermode.commands.DeveloperModeCommand;
import dLib.commands.objects.OpenScreenCommand;

public class CommandManager {
    public static void initialize(){
        ConsoleCommand.addCommand("developermode", DeveloperModeCommand.class);
        ConsoleCommand.addCommand("openscreen", OpenScreenCommand.class);
    }
}
