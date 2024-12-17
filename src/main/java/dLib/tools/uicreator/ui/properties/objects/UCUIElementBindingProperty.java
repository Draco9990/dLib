package dLib.tools.uicreator.ui.properties.objects;

import dLib.properties.objects.templates.TProperty;
import dLib.ui.bindings.UIElementBinding;
import dLib.tools.uicreator.ui.properties.editors.UCUIElementBindingValueEditor;

public class UCUIElementBindingProperty extends TProperty<UIElementBinding, UCUIElementBindingProperty> {
    public UCUIElementBindingProperty(UIElementBinding value) {
        super(value);
    }

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("UIElementBindingProperty does not support setting values from strings");
    }
}
