package dLib.custominput;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.screens.options.InputSettingsScreen;
import com.megacrit.cardcrawl.screens.options.RemapInputElement;
import dLib.patches.KeyInputEventPatches;
import dLib.util.Reflection;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class CustomKeybindManager {
    public static LinkedHashMap<String, CustomKeybindData> actionMap = new LinkedHashMap<>();

    public static void registerCommonEvents(){
        KeyInputEventPatches.onKeyPressed.subscribeManaged(integer -> {
            for (Map.Entry<String, CustomKeybindData> action : CustomKeybindManager.actionMap.entrySet()) {
                if ((action.getValue().inputAction != null && action.getValue().inputAction.isJustPressed()) || (action.getValue().cInputAction != null && action.getValue().cInputAction.isJustPressed())) {
                    action.getValue().onKeyPressed.run();
                }
            }
        });
    }

    public static void register(String actionId, Function<String, String> getLocalizedDisplayName, InputAction inputAction, CInputAction cInputAction, Runnable onKeyPressed) {
        if (actionMap == null) {
            actionMap = new LinkedHashMap<>();
        }

        int actionKey = InputActionSet.prefs.getInteger(actionId, -1);
        if (actionKey != -1) {
            inputAction.remap(actionKey);
        }

        int cActionKey = CInputActionSet.prefs.getInteger(actionId, -1);
        if (cActionKey != -1) {
            cInputAction.remap(cActionKey);
        }

        actionMap.put(actionId, new CustomKeybindData(getLocalizedDisplayName, inputAction, cInputAction, onKeyPressed));
    }

    public static InputAction getAction(String actionId){
        if (actionMap == null) {
            actionMap = new LinkedHashMap<>();
        }
        if (!actionMap.containsKey(actionId)){
            return null;
        }

        return actionMap.get(actionId).inputAction;
    }

    public static CInputAction getCAction(String actionId){
        if (actionMap == null) {
            actionMap = new LinkedHashMap<>();
        }
        if (!actionMap.containsKey(actionId)){
            return null;
        }

        return actionMap.get(actionId).cInputAction;
    }

    private static class CustomKeybindData {
        public CustomKeybindData(Function<String, String> getLocalizedDisplayName, InputAction inputAction, CInputAction cInputAction, Runnable onKeyPressed) {
            this.getLocalizedDisplayName = getLocalizedDisplayName;
            this.inputAction = inputAction;
            this.cInputAction = cInputAction;
            this.onKeyPressed = onKeyPressed;
        }

        public Function<String, String> getLocalizedDisplayName;

        public InputAction inputAction;
        public CInputAction cInputAction;

        public Runnable onKeyPressed;
    }

    public static class Patches{
        @SpirePatch2(clz = InputActionSet.class, method = "save")
        public static class SaveInputSet {
            @SpirePrefixPatch
            public static void Prefix() {
                for (Map.Entry<String, CustomKeybindData> action : CustomKeybindManager.actionMap.entrySet()) {
                    InputAction actionValue = action.getValue().inputAction;
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
                for (Map.Entry<String, CustomKeybindData> action : CustomKeybindManager.actionMap.entrySet()) {
                    CInputAction actionValue = action.getValue().cInputAction;
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

                for (Map.Entry<String, CustomKeybindData> action : CustomKeybindManager.actionMap.entrySet()) {
                    elements.add(new RemapInputElement(__instance, action.getValue().getLocalizedDisplayName.apply(action.getKey()), action.getValue().inputAction, action.getValue().cInputAction));
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
}
