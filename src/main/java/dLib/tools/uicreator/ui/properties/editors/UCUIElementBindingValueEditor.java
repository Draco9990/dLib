package dLib.tools.uicreator.ui.properties.editors;

import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.tools.uicreator.UCEditor;
import dLib.ui.bindings.UIElementBinding;
import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.screens.UIManager;

public abstract class UCUIElementBindingValueEditor<BindingValue extends UIElementBinding> extends AbstractValueEditor<BindingValue, UCUIElementBindingProperty> {
    public UCUIElementBindingValueEditor(UCUIElementBindingProperty property) {
        super(property);

        if(UIManager.getOpenElementOfType(UCEditor.class) == null){
            throw new UnsupportedOperationException("UCUIBindingPropertyEditor only works within UI element creation editors");
        }
    }
}
