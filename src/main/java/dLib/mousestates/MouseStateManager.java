package dLib.mousestates;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.ArrayList;

public class MouseStateManager {
    //region Singleton

    private static MouseStateManager instance;

    public static MouseStateManager get() {
        if (instance == null) {
            instance = new MouseStateManager();
        }
        return instance;
    }

    //endregion

    //region Variables

    private MouseState currentState;

    //endregion

    //region Constructor

    private MouseStateManager() {
    }

    //endregion

    //region Methods

    public boolean enterMouseState(MouseState requestedState) {
        if(currentState != null) {
            return false;
        }

        currentState = requestedState;
        currentState.onStateEnter();

        return true;
    }

    public boolean exitMouseState() {
        if(currentState == null) {
            return false;
        }

        currentState.onStateExit();
        currentState.dispose();
        currentState = null;

        return true;
    }

    //endregion

    //region Patches

    @SpirePatch2(clz = InputHelper.class, method = "updateFirst")
    public static class MouseStateUpdater {
        public static void Postfix() {
            MouseStateManager manager = MouseStateManager.get();
            if(manager.currentState != null) {
                manager.currentState.update();
            }
        }
    }

    //endregion
}
