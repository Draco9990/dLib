package dLib.util.bindings.method;

import dLib.ui.screens.ScreenManager;

import java.io.Serializable;

public class CloseScreenMethodBinding extends MethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

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
