package dLib.ui.bindings;

import dLib.properties.objects.StringProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.tools.uicreator.ui.properties.editors.UCRelativeUIElementBindingValueEditor;
import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class UIElementUndefinedPathBinding extends UIElementPathBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    public UIElementUndefinedPathBinding() {
        super();
    }

    @Override
    public UIElement getBoundObject(Object... params) {
        return null;
    }

    @Override
    public boolean isBindingValid() {
        return false;
    }

    @Override
    public String getDisplayValue() {
        return "none";
    }
}
