package dLib.mainmenubuttons;

import dLib.util.bindings.string.AbstractStringBinding;

import java.util.Map;

public class MainMenuButtonManager {
    private static Map<AbstractStringBinding, Runnable> buttonActions;

    public static void initialize() {
        //registerButtonAction();
    }

    public static void registerButtonAction(AbstractStringBinding buttonLabel, Runnable action) {
        if (buttonActions == null) {
            buttonActions = new java.util.HashMap<>();
        }
        buttonActions.put(buttonLabel, action);
    }
}
