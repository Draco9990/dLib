package dLib.util.bindings.method.staticbindings;

import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class CloseTopmostMethodBinding extends StaticMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Close Topmost";

    /** Binding */
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Object executeBinding(Object invoker, Object... args) {
        if(invoker instanceof UIElement){
            ((UIElement) invoker).getTopParent().close();
        }
        return null;
    }

    /** Name */
    @Override
    public String getShortDisplayName() {
        return PROPERTY_EDITOR_LONG_NAME;
    }

    @Override
    public String getFullDisplayName() {
        return getShortDisplayName();
    }
}
