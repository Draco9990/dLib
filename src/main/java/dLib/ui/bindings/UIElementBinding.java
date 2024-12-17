package dLib.ui.bindings;

import dLib.properties.ui.elements.IEditableValue;
import dLib.ui.elements.UIElement;
import dLib.util.bindings.ResourceBinding;

import java.io.Serializable;

public abstract class UIElementBinding extends ResourceBinding<UIElement> implements Serializable, IEditableValue {
    private static final long serialVersionUID = 1L;
}
