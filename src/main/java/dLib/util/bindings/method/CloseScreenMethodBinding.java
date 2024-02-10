package dLib.util.bindings.method;

import dLib.ui.screens.ScreenManager;

public class CloseScreenMethodBinding extends MethodBinding{
    /** Binding */
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Object executeBinding(Object invoker, Object... args) {
        ScreenManager.closeScreen();
        return null;
    }

    /** Name */
    @Override
    public String getShortDisplayName() {
        return "Close Screen";
    }

    @Override
    public String getFullDisplayName() {
        return getShortDisplayName();
    }
}
