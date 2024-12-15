package dLib.ui.bindings;

import dLib.ui.elements.UIElement;
import dLib.util.bindings.Binding;
import dLib.util.bindings.ResourceBinding;

import java.io.Serializable;

public abstract class UIBinding extends ResourceBinding<UIElement> implements Serializable {
    private static final long serialVersionUID = 1L;
}
