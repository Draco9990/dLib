package dLib.util.bindings.method;

import dLib.ui.elements.UIElement;
import dLib.ui.screens.UIManager;

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
        if(invoker instanceof UIElement){
            ((UIElement) invoker).close();
        }
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
