package dLib.mousestates;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.properties.objects.templates.TProperty;
import dLib.util.HistoryProperty;

public class MouseStateManager {
    //region Variables

    private static HistoryProperty<AbstractMouseState> currentState = new HistoryProperty<>(null);

    //endregion

    //region Methods

    public static boolean enterMouseState(AbstractMouseState requestedState) {
        if(getCurrentState() != null) {
            exitMouseState_Internal(false);
        }

        enterMouseState_Internal(requestedState);

        return true;
    }

    public static boolean exitMouseState() {
        if(currentState.get() == null) {
            return false;
        }

        AbstractMouseState reverted = exitMouseState_Internal(true);
        reverted.dispose();

        return true;
    }

    public static void insertQueuedMouseState(AbstractMouseState requestedState) {
        if(currentState.get() == null){
            return;
        }

        currentState.insertBeforeTop(requestedState);
    }

    public static void removeFromQueuedMouseStates(Class<? extends AbstractMouseState> state) {
        if(currentState.get() == null){
            return;
        }

        currentState.removeAnyClassInstance(state);
    }

    private static AbstractMouseState exitMouseState_Internal(boolean revert){
        currentState.get().preStateExitEvent.invoke();
        AbstractMouseState.preStateExitGlobalEvent.invoke(currentState.get());

        currentState.get().onStateExit();
        AbstractMouseState exited;
        if(revert) {
            exited = currentState.revert();
        }
        else{
            exited = currentState.get();
        }

        exited.postStateExitEvent.invoke();
        AbstractMouseState.postStateExitGlobalEvent.invoke(exited);

        return exited;
    }

    private static void enterMouseState_Internal(AbstractMouseState state){
        state.preStateEnterEvent.invoke();
        AbstractMouseState.preStateEnterGlobalEvent.invoke(state);

        currentState.set(state);
        state.onStateEnter();

        state.postStateEnterEvent.invoke();
        AbstractMouseState.postStateEnterGlobalEvent.invoke(state);
    }

    public static boolean isInExternalState() {
        return getCurrentState() != null;
    }

    public static AbstractMouseState getCurrentState() {
        return currentState.get();
    }

    //endregion

    //region Patches

    @SpirePatch2(clz = InputHelper.class, method = "updateFirst")
    public static class MouseStateUpdater {
        public static void Postfix() {
            if(getCurrentState() != null) {
                getCurrentState().update();
            }
        }
    }

    //endregion
}
