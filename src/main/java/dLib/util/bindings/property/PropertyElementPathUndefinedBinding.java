package dLib.util.bindings.property;

import dLib.properties.objects.Property;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.elements.UIElement;
import dLib.util.Reflection;
import dLib.util.bindings.property.editors.PropertyElementPathBindingValueEditor;

import java.io.Serializable;

public class PropertyElementPathUndefinedBinding extends PropertyElementPathBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    public PropertyElementPathUndefinedBinding(){
        super("", "none");
    }

    @Override
    public TProperty getBoundObject(Object... params) {
        return null;
    }

    @Override
    public String getDisplayValue() {
        return "NONE";
    }
}
