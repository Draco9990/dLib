package dLib.ui.bindings;

import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class UIElementUndefinedRelativePathBinding extends UIElementRelativePathBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    public UIElementUndefinedRelativePathBinding() {
        super();
    }

    @Override
    public UIElement resolve(Object... params) {
        return null;
    }

    @Override
    public boolean isBindingValid() {
        return false;
    }

    @Override
    public String toString() {
        return "none";
    }
}
