package dLib.mainmenubuttons;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import dLib.util.Reflection;
import dLib.util.bindings.string.AbstractStringBinding;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class MainMenuButtonManager {
    private static Map<MenuButton.ClickResult, CustomButtonAction> customButtons = new HashMap<>();
    private static Map<MenuButton.ClickResult, Pair<AbstractStringBinding, Supplier<Boolean>>> buttonNameOverrides = new HashMap<>();
    private static Map<MenuButton.ClickResult, Pair<Runnable, Supplier<Boolean>>> buttonActionOverrides = new HashMap<>();

    public static void registerCustomMainMenuButton(MenuButton.ClickResult clickResult, ArrayList<MenuButton.ClickResult> insertAfter, AbstractStringBinding buttonLabel, Runnable action, Supplier<Boolean> isVisible) {
        if (customButtons.containsKey(clickResult)) {
            throw new IllegalArgumentException("Button action for " + clickResult + " is already registered.");
        }
        customButtons.put(clickResult, new CustomButtonAction(insertAfter, buttonLabel, action, isVisible));
    }

    public static void registerMainMenuButtonNameOverride(MenuButton.ClickResult result, AbstractStringBinding newName, Supplier<Boolean> isVisible) {
        if (buttonNameOverrides.containsKey(result)) {
            throw new IllegalArgumentException("Button name override for " + result + " is already registered.");
        }
        buttonNameOverrides.put(result, new Pair<>(newName, isVisible));
    }

    public static void registerMainMenuButtonActionOverride(MenuButton.ClickResult result, Runnable action, Supplier<Boolean> isVisible) {
        if (buttonActionOverrides.containsKey(result)) {
            throw new IllegalArgumentException("Button action override for " + result + " is already registered.");
        }
        buttonActionOverrides.put(result, new Pair<>(action, isVisible));
    }

    private static class CustomButtonAction{
        private ArrayList<MenuButton.ClickResult> insertAfter;
        private AbstractStringBinding buttonLabel;
        private Runnable action;
        private Supplier<Boolean> isVisible;

        public CustomButtonAction(ArrayList<MenuButton.ClickResult> insertAfter, AbstractStringBinding buttonLabel, Runnable action, Supplier<Boolean> isVisible) {
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

                customButtons.forEach((clickResult, customAction) -> {
                    if (customAction.insertAfter.contains(b.result) && customAction.isVisible.get()) {
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
            if(customButtons.containsKey(__instance.result)) {
                Reflection.setFieldValue("label", __instance, customButtons.get(__instance.result).buttonLabel.getBoundObject());
            }
            else if(buttonNameOverrides.containsKey(__instance.result) && buttonNameOverrides.get(__instance.result).getValue().get()) {
                Reflection.setFieldValue("label", __instance, buttonNameOverrides.get(__instance.result).getKey().getBoundObject());
            }
        }
    }

    @SpirePatch(clz = MenuButton.class, method = "buttonEffect")
    public static class EffectPatcher{
        public static SpireReturn Prefix(MenuButton __instance){
            if(customButtons.containsKey(__instance.result)){
                customButtons.get(__instance.result).action.run();

                return SpireReturn.Return();
            }
            if(buttonActionOverrides.containsKey(__instance.result) && buttonActionOverrides.get(__instance.result).getValue().get()) {
                buttonActionOverrides.get(__instance.result).getKey().run();
                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }
}
