package dLib.util.bindings.texture.editors;

import dLib.properties.objects.TextureBindingProperty;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.elements.items.UIResourcePicker;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.TextureResourceBinding;
import dLib.util.ui.dimensions.Dim;

public class TextureResourceBindingValueEditor extends TextureBindingValueEditor<TextureResourceBinding> {
    private TextButton valueBox;

    public TextureResourceBindingValueEditor(TextureResourceBinding binding) {
        this(new TextureBindingProperty(binding));
    }

    public TextureResourceBindingValueEditor(TextureBindingProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            valueBox = new TextButton(property.getValue().getDisplayValue(), Dim.fill(), Dim.fill());
            valueBox.setImage(Tex.stat(UICommonResources.button02_horizontal));
            valueBox.onLeftClickEvent.subscribe(this, () -> {
                UIResourcePicker picker = new UIResourcePicker((aClass, s) -> property.setValue(new TextureResourceBinding(aClass, s)));
                picker.open();
            });
            contentBox.addItem(valueBox);
            contentBox.addItem(makeSwapComboBox());
        }

        property.onValueChangedEvent.subscribe(this, (textureBinding, textureBinding2) -> {
            if (!isEditorValidForPropertyChange()) return;

            valueBox.label.setText(textureBinding2.getDisplayValue());
        });

        addChildNCS(contentBox);
    }
}
