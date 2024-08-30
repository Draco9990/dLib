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
                InputAction actionValue = action.getValue().getValue().getKey();
                if(actionValue == null){
                    continue;
                }

                InputActionSet.prefs.putInteger(action.getKey(), actionValue.getKey());
            }
        }
    }

    @SpirePatch2(clz = CInputActionSet.class, method = "save")
    public static class SaveCInputSet {
        @SpirePrefixPatch
        public static void Prefix() {
            for (Map.Entry<String, Pair<Function<String, String>, Pair<InputAction, CInputAction>>> action : CustomInputSetManager.actionMap.entrySet()) {
                CInputAction actionValue = action.getValue().getValue().getValue();
                if(actionValue == null){
                    continue;
                }

                CInputActionSet.prefs.putInteger(action.getKey(), actionValue.getKey());
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
