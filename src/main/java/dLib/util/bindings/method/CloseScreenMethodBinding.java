package dLib.util.bindings.method;

import dLib.ui.elements.UIElement;
import dLib.ui.screens.UIManager;

import java.io.Serializable;

public class CloseScreenMethodBinding extends StaticMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Close";

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
