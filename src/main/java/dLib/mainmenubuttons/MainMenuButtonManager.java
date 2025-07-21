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
import dLib.util.bindings.string.AbstractStringBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class MainMenuButtonManager {
    private static Map<MenuButton.ClickResult, CustomButtonAction> buttonActions = new HashMap<>();

    public static void registerButtonAction(MenuButton.ClickResult clickResult, MenuButton.ClickResult insertAfter, AbstractStringBinding buttonLabel, Runnable action, Supplier<Boolean> isVisible) {
        if (buttonActions.containsKey(clickResult)) {
            throw new IllegalArgumentException("Button action for " + clickResult + " is already registered.");
        }
        buttonActions.put(clickResult, new CustomButtonAction(insertAfter, buttonLabel, action, isVisible));
    }

    private static class CustomButtonAction{
        private MenuButton.ClickResult insertAfter;
        private AbstractStringBinding buttonLabel;
        private Runnable action;
        private Supplier<Boolean> isVisible;

        public CustomButtonAction(MenuButton.ClickResult insertAfter, AbstractStringBinding buttonLabel, Runnable action, Supplier<Boolean> isVisible) {
            this.insertAfter = insertAfter;
            this.buttonLabel = buttonLabel;
            this.action = action;
            this.isVisible = isVisible;
        }
    }

    @SpirePatch2(clz = MainMenuScreen.class, method = "setMainMenuButtons")
    public static class AddMultiplayerButton{
        public static void Postfix(MainMenuScreen __instance){
            ArrayList<MenuButton> buttons = new ArrayList<>(__instance.buttons);
            __instance.buttons.clear();

            AtomicInteger indx = new AtomicInteger();
            for (int i = 0; i < buttons.size(); i++) {
                MenuButton b = buttons.get(i);

                buttonActions.forEach((clickResult, customAction) -> {
                    if (b.result == customAction.insertAfter && customAction.isVisible.get()) {
                        __instance.buttons.add(new MenuButton(clickResult, indx.getAndIncrement()));
                    }
                });

                __instance.buttons.add(new MenuButton(b.result, indx.getAndIncrement()));
            }
        }
    }

    @SpirePatch(clz = MenuButton.class, method = "setLabel")
    public static class LabelPatcher{
        public static void Postfix(MenuButton __instance, String ___label){
            if(buttonActions.containsKey(__instance.result)) {
                Reflection.setFieldValue("label", __instance, buttonActions.get(__instance.result).buttonLabel.getBoundObject());
            }
        }
    }

    @SpirePatch(clz = MenuButton.class, method = "buttonEffect")
    public static class EffectPatcher{
        public static SpireReturn Prefix(MenuButton __instance){
            if(buttonActions.containsKey(__instance.result)){
                buttonActions.get(__instance.result).action.run();

                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
