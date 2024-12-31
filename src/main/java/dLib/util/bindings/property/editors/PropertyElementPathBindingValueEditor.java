package dLib.util.bindings.property.editors;

import dLib.properties.objects.Property;
import dLib.properties.objects.TextureBindingProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.resourcepicker.UIResourcePicker;
import dLib.ui.elements.items.resourcepicker.UITextureResourcePicker;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.property.AbstractPropertyBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.TextureResourceBinding;
import dLib.util.ui.dimensions.Dim;

public class PropertyElementPathBindingValueEditor extends AbstractValueEditor<AbstractPropertyBinding, Property<AbstractPropertyBinding>> {
    private TextButton valueBox;

    public PropertyElementPathBindingValueEditor(AbstractPropertyBinding binding) {
        this(new Property<>(binding));
    }

    public PropertyElementPathBindingValueEditor(Property<AbstractPropertyBinding> property) {
        super(property);

        valueBox = new TextButton(property.getValue().getDisplayValue(), Dim.fill(), Dim.px(50));
        valueBox.setImage(Tex.stat(UICommonResources.button02_horizontal));
        valueBox.onLeftClickEvent.subscribe(this, () -> {
        });
        addChildNCS(valueBox);

        property.onValueChangedEvent.subscribe(this, (textureBinding, textureBinding2) -> {
            if (!isEditorValidForPropertyChange()) return;

            valueBox.label.setText(textureBinding2.getDisplayValue());
        });
    }
}
