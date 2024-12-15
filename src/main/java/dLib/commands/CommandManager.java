package dLib.commands;

import basemod.devcommands.ConsoleCommand;
import dLib.commands.objects.TestCommand;
import dLib.commands.objects.ToggleUIDebuggerCommand;
import dLib.developermode.commands.DeveloperModeCommand;
import dLib.commands.objects.OpenScreenCommand;

public class CommandManager {
    public static void initialize(){
        ConsoleCommand.addCommand("developermode", DeveloperModeCommand.class);
        ConsoleCommand.addCommand("uidebug", ToggleUIDebuggerCommand.class);
        ConsoleCommand.addCommand("dtest", TestCommand.class);
    }
}
