package dLib.custominput;

import basemod.Pair;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.options.InputSettingsScreen;
import com.megacrit.cardcrawl.screens.options.RemapInputElement;
import dLib.util.Reflection;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class InputSetPatches {
    @SpirePatch2(clz = InputActionSet.class, method = "save")
    public static class SaveInputSet {
        @SpirePrefixPatch
        public static void Prefix() {
            for (Map.Entry<String, Pair<Function<String, String>, Pair<InputAction, CInputAction>>> action : CustomInputSetManager.actionMap.entrySet()) {
                InputActionSet.prefs.putInteger(action.getKey(), action.getValue().getValue().getKey().getKey());
            }
        }
    }

    @SpirePatch2(clz = CInputActionSet.class, method = "save")
    public static class SaveCInputSet {
        @SpirePrefixPatch
        public static void Prefix() {
            for (Map.Entry<String, Pair<Function<String, String>, Pair<InputAction, CInputAction>>> action : CustomInputSetManager.actionMap.entrySet()) {
                CInputActionSet.prefs.putInteger(action.getKey(), action.getValue().getValue().getValue().getKey());
            }
        }
    }

    @SpirePatch2(clz = CInputActionSet.class, method = "load")
    public static class LoadCInputSet {
        @SpirePrefixPatch
        public static void Prefix() {
            for (Map.Entry<String, Pair<Function<String, String>, Pair<InputAction, CInputAction>>> action : CustomInputSetManager.actionMap.entrySet()) {
                int key = CInputActionSet.prefs.getInteger(action.getKey(), -1);
                if(key == -1){
                    continue;
                }

                action.getValue().getValue().getValue().remap(key);
            }
        }
    }

    @SpirePatch2(clz = InputSettingsScreen.class, method = "refreshData")
    public static class DisplayInputSets{
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(InputSettingsScreen __instance){
            ArrayList<RemapInputElement> elements = Reflection.getFieldValue("elements", __instance);

            for (Map.Entry<String, Pair<Function<String, String>, Pair<InputAction, CInputAction>>> action : CustomInputSetManager.actionMap.entrySet()) {
                elements.add(new RemapInputElement(__instance, action.getValue().getKey().apply(action.getKey()), action.getValue().getValue().getKey(), action.getValue().getValue().getValue()));
            }

            Reflection.setFieldValue("elements", __instance, elements);
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "size");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
