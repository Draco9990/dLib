package dLib.mainmenubuttons;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import dLib.developermode.DeveloperModeManager;
import dLib.external.ExternalEditorCommunicationManager;
import dLib.test.TestScreen;
import dLib.tools.uicreator.UCEditor;
import dLib.tools.uicreator.UCStartupPopup;
import dLib.util.DLibConfigManager;
import dLib.util.Reflection;

import java.util.ArrayList;

public class MainMenuButtonPatches {
    //Patches functionality to new buttons
    @SpirePatch(clz = MenuButton.class, method = "buttonEffect")
    public static class EffectPatcher{
        public static SpireReturn Prefix(MenuButton __instance){
            if(__instance.result == DeveloperModeManager.Enums.DEVELOPER){
                if(!ExternalEditorCommunicationManager.isEnabled()) ExternalEditorCommunicationManager.enable();
                if(!ExternalEditorCommunicationManager.isRunning()) ExternalEditorCommunicationManager.start();

                if(false && ExternalEditorCommunicationManager.isRunning()){
                    UCStartupPopup popup = new UCStartupPopup();
                    popup.open();
                }
                else if(true){
                    UCEditor editor = new UCEditor();
                    editor.open();
                }
                else{
                    TestScreen testScreen = new TestScreen();
                    testScreen.open();
                }

                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

}
