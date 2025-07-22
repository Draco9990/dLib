package dLib.mousestates;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class MouseStateManager {
    //region Variables

    private static AbstractMouseState currentState;

    //endregion

    //region Methods

    public static boolean enterMouseState(AbstractMouseState requestedState) {
        if(currentState != null) {
            return false;
        }

        requestedState.preStateEnterEvent.invoke();
        AbstractMouseState.preStateEnterGlobalEvent.invoke(requestedState);

        currentState = requestedState;
        currentState.onStateEnter();

        currentState.postStateEnterEvent.invoke();
        AbstractMouseState.postStateEnterGlobalEvent.invoke(currentState);

        return true;
    }

    public static boolean exitMouseState() {
        if(currentState == null) {
            return false;
        }

        currentState.preStateExitEvent.invoke();
        AbstractMouseState.preStateExitGlobalEvent.invoke(currentState);

        currentState.onStateExit();
        currentState.dispose();
        currentState = null;

        currentState.postStateExitEvent.invoke();
        AbstractMouseState.postStateExitGlobalEvent.invoke(currentState);

        return true;
    }

    public static boolean isInExternalState() {
        return currentState != null;
    }

    public static AbstractMouseState getCurrentState() {
        return currentState;
    }

    //endregion

    //region Patches

    @SpirePatch2(clz = InputHelper.class, method = "updateFirst")
    public static class MouseStateUpdater {
        public static void Postfix() {
            if(currentState != null) {
                currentState.update();
            }
        }
    }

    //endregion
}
