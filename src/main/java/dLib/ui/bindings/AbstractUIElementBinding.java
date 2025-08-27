package dLib.ui.bindings;

import dLib.ui.elements.UIElement;
import dLib.util.bindings.Binding;

import java.io.Serializable;

public abstract class AbstractUIElementBinding extends Binding<UIElement> implements Serializable {
    private static final long serialVersionUID = 1L;

    public abstract boolean isBindingValid();
}
