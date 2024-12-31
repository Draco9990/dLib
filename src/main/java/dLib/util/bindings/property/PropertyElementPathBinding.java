package dLib.util.bindings.property;

import dLib.properties.objects.Property;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.bindings.AbstractUIElementBinding;
import dLib.ui.elements.UIElement;
import dLib.util.Reflection;
import dLib.util.bindings.property.editors.PropertyElementPathBindingValueEditor;

import java.io.Serializable;

public class PropertyElementPathBinding extends AbstractPropertyBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Property Path";
    private static final String PROPERTY_EDITOR_SHORT_NAME = "path";

    //region Variables

    private final AbstractUIElementBinding elementBinding;
    private final String propertyName;

    //endregion Variables

    public PropertyElementPathBinding(AbstractUIElementBinding elementBinding, String propertyName){
        this.elementBinding = elementBinding;
        this.propertyName = propertyName;
    }

    @Override
    public TProperty getBoundObject(Object... params) {
        return Reflection.getFieldValue(propertyName, elementBinding.getBoundObject(params[0]));
    }

    @Override
    public String getDisplayValue() {
        return elementBinding.getDisplayValue() + "/" + propertyName;
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PropertyElementPathBindingValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PropertyElementPathBindingValueEditor((Property<AbstractPropertyBinding>) property);
    }
}
