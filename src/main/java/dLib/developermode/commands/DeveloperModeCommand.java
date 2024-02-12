package dLib.developermode.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import dLib.util.DLibConfigManager;
import dLib.util.DLibLogger;
import dLib.util.Reflection;

import java.io.IOException;

public class DeveloperModeCommand extends ConsoleCommand {
    public DeveloperModeCommand() {
        this.requiresPlayer = false;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        if(tokens.length < 2) return;

        if(tokens[1].equals("enable")){
            DLibConfigManager.get().setBool(DLibConfigManager.DEV_MODE, true);
        }
        else if(tokens[1].equals("disable")){
            DLibConfigManager.get().setBool(DLibConfigManager.DEV_MODE, false);
        }

        try {
            DLibConfigManager.get().save();
        } catch (IOException e) {
            DLibLogger.log("Could not save new config due to " + e.getMessage());
            e.printStackTrace();
        }

        if(CardCrawlGame.mode == CardCrawlGame.GameMode.CHAR_SELECT){
            Reflection.invokeMethod("setMainMenuButtons", CardCrawlGame.mainMenuScreen);
        }
    }

    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
    }
}