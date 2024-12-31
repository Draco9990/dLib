package dLib.tools.uicreator.ui.properties.objects;

import dLib.properties.objects.templates.TProperty;
import dLib.ui.bindings.AbstractUIElementBinding;

public class UCUIElementBindingProperty extends TProperty<AbstractUIElementBinding, UCUIElementBindingProperty> {
    public UCUIElementBindingProperty(AbstractUIElementBinding value) {
        super(value);
    }

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("UIElementBindingProperty does not support setting values from strings");
    }
}
