package dLib.custominput;

import basemod.Pair;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import dLib.util.BiMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class CustomInputSetManager {
    public static LinkedHashMap<String, Pair<Function<String, String>, Pair<InputAction, CInputAction>>> actionMap = new LinkedHashMap<>();

    public static void register(String actionId, Function<String, String> getLocalizedDisplayName, InputAction inputAction, CInputAction cInputAction) {
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

        actionMap.put(actionId, new Pair<>(getLocalizedDisplayName, new Pair<>(inputAction, cInputAction)));
    }

    public static InputAction getAction(String actionId){
        if (actionMap == null) {
            actionMap = new LinkedHashMap<>();
        }
        if (!actionMap.containsKey(actionId)){
            return null;
        }

        return actionMap.get(actionId).getValue().getKey();
    }

    public static CInputAction getCAction(String actionId){
        if (actionMap == null) {
            actionMap = new LinkedHashMap<>();
        }
        if (!actionMap.containsKey(actionId)){
            return null;
        }

        return actionMap.get(actionId).getValue().getValue();
    }
}
