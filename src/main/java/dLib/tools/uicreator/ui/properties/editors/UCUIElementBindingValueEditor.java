package dLib.tools.uicreator.ui.properties.editors;

import com.badlogic.gdx.graphics.Texture;
import dLib.mousestates.MouseStateManager;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.tools.uicreator.UCEditor;
import dLib.ui.bindings.RelativeUIElementBinding;
import dLib.ui.bindings.UIElementBinding;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.mousestates.ReferencePickerMouseState;
import dLib.ui.resources.UICommonResources;
import dLib.ui.screens.UIManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;

public abstract class UCUIElementBindingValueEditor<BindingValue extends UIElementBinding> extends AbstractValueEditor<BindingValue, UCUIElementBindingProperty> {
    public UCUIElementBindingValueEditor(UCUIElementBindingProperty property, AbstractDimension width, AbstractDimension height) {
        super(property, width, height);

        if(UIManager.getOpenElementOfType(UCEditor.class) == null){
            throw new UnsupportedOperationException("UCUIBindingPropertyEditor only works within UI element creation editors");
        }
    }
}
