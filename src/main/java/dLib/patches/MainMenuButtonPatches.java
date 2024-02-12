package dLib.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import dLib.developermode.ui.screens.InstallPluginScreen;
import dLib.ui.screens.ScreenManager;
import dLib.util.DLibConfigManager;
import dLib.util.Reflection;

import java.util.ArrayList;

public class MainMenuButtonPatches {
    @SpirePatch2(clz = MainMenuScreen.class, method = "setMainMenuButtons")
    public static class AddMultiplayerButton{
        public static void Postfix(MainMenuScreen __instance){
            ArrayList<MenuButton> buttons = (ArrayList<MenuButton>)__instance.buttons.clone();
            __instance.buttons.clear();
            int indx = 0;
            for(MenuButton b : buttons){
                if(b.result == MenuButton.ClickResult.SETTINGS && DLibConfigManager.get().getBool(DLibConfigManager.DEV_MODE)){
                    __instance.buttons.add(new MenuButton(Enums.DEVELOPER, indx++));
                }
                __instance.buttons.add(new MenuButton(b.result, indx++));
            }
        }
    }

    @SpirePatch(clz = MenuButton.class, method = "setLabel")
    public static class LabelPatcher{
        public static void Postfix(MenuButton __instance, String ___label){
            if(__instance.result == Enums.DEVELOPER) Reflection.setFieldValue("label", __instance, "Developer");
        }
    }

    //Patches functionality to new buttons
    @SpirePatch(clz = MenuButton.class, method = "buttonEffect")
    public static class EffectPatcher{
        public static SpireReturn Prefix(MenuButton __instance){
            if(__instance.result == Enums.DEVELOPER){
                ScreenManager.openScreen(new InstallPluginScreen());
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    public static class Enums {
        @SpireEnum
        public static MenuButton.ClickResult DEVELOPER;
    }
}
