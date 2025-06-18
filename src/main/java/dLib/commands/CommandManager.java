package dLib.commands;

import basemod.devcommands.ConsoleCommand;
import dLib.commands.objects.CSHirearchyViewerOpenerCommand;
import dLib.commands.objects.TestCommand;
import dLib.commands.objects.ToggleUIDebuggerCommand;
import dLib.developermode.commands.DeveloperModeCommand;

public class CommandManager {
    public static void initialize(){
        ConsoleCommand.addCommand("developermode", DeveloperModeCommand.class);
        ConsoleCommand.addCommand("uidebug", ToggleUIDebuggerCommand.class);
        ConsoleCommand.addCommand("csdebug", CSHirearchyViewerOpenerCommand.class);
        ConsoleCommand.addCommand("dtest", TestCommand.class);
    }
}
