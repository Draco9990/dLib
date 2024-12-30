package dLib.mousestates;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.mousestates.events.PostEnterMouseStateEvent;
import dLib.ui.mousestates.events.PreExitMouseStateEvent;
import dLib.util.events.GlobalEvents;

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

    private AbstractMouseState currentState;

    //endregion

    //region Constructor

    private MouseStateManager() {
    }

    //endregion

    //region Methods

    public boolean enterMouseState(AbstractMouseState requestedState) {
        if(currentState != null) {
            return false;
        }

        currentState = requestedState;
        currentState.onStateEnter();

        GlobalEvents.sendMessage(new PostEnterMouseStateEvent(currentState));

        return true;
    }

    public boolean exitMouseState() {
        if(currentState == null) {
            return false;
        }

        GlobalEvents.sendMessage(new PreExitMouseStateEvent(currentState));

        currentState.onStateExit();
        currentState.dispose();
        currentState = null;

        return true;
    }

    public boolean isInExternalState() {
        return currentState != null;
    }

    public AbstractMouseState getCurrentState() {
        return currentState;
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
