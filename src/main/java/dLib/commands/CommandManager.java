package dLib.commands;

import basemod.devcommands.ConsoleCommand;
import dLib.developermode.commands.DeveloperModeCommand;
import dLib.commands.objects.OpenScreenCommand;
import dLib.tools.screeneditor.commands.OpenScreenEditorCommand;
import dLib.plugin.intellij.commands.IntelliJCommand;
import dLib.plugin.intellij.commands.TestCommand;

public class CommandManager {

    public static void initialize(){
        ConsoleCommand.addCommand("developermode", DeveloperModeCommand.class);
        ConsoleCommand.addCommand("openscreen", OpenScreenCommand.class);
        ConsoleCommand.addCommand("screeneditor", OpenScreenEditorCommand.class);
        ConsoleCommand.addCommand("intellij", IntelliJCommand.class);
        ConsoleCommand.addCommand("test", TestCommand.class);
    }
}
