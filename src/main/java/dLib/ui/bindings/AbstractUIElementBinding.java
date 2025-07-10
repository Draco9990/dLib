package dLib.ui.bindings;

import dLib.properties.ui.elements.IEditableValue;
import dLib.ui.elements.UIElement;
import dLib.util.bindings.ResourceBinding;

import java.io.Serializable;

public abstract class AbstractUIElementBinding extends ResourceBinding<UIElement> implements Serializable {
    private static final long serialVersionUID = 1L;

    public abstract boolean isBindingValid();
}
