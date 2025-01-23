package dLib.util.bindings.property.editors;

import dLib.properties.objects.Property;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.elements.items.resourcepicker.UIObjectPropertyResourcePicker;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.property.AbstractPropertyBinding;
import dLib.util.bindings.property.PropertyElementPathBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

public class PropertyElementPathBindingValueEditor extends AbstractValueEditor<AbstractPropertyBinding, Property<AbstractPropertyBinding>> {
    private TextButton valueBox;

    public PropertyElementPathBindingValueEditor(AbstractPropertyBinding binding) {
        this(new Property<>(binding));
    }

    public PropertyElementPathBindingValueEditor(Property<AbstractPropertyBinding> property) {
        super(property);

        valueBox = new TextButton(property.getValue().getDisplayValue(), Dim.fill(), Dim.px(50));
        valueBox.setImage(Tex.stat(UICommonResources.button02_square));
        valueBox.onLeftClickEvent.subscribe(this, () -> {
            UIObjectPropertyResourcePicker resourcePicker = new UIObjectPropertyResourcePicker(((PropertyElementPathBinding)boundProperty.getValue()).elementBinding.getBoundObject());
            resourcePicker.onResourceSelectedEvent.subscribe(this, (aClass, s) -> {
                AbstractPropertyBinding propertyBinding = boundProperty.getValue();
                if(!(propertyBinding instanceof PropertyElementPathBinding)) {
                    return;
                }

                boundProperty.setValue(new PropertyElementPathBinding(((PropertyElementPathBinding) propertyBinding).elementBinding, s));
            });
            resourcePicker.open();
        });
        addChild(valueBox);

        property.onValueChangedEvent.subscribe(this, (textureBinding, propertyBinding) -> {
            if (!isEditorValidForPropertyChange()) return;

            valueBox.label.setText(propertyBinding.getDisplayValue());
        });
    }
}
