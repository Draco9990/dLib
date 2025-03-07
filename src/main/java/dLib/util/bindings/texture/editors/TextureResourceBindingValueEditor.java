package dLib.util.bindings.texture.editors;

import dLib.properties.objects.TextureBindingProperty;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.resourcepicker.AbstractUIResourcePicker;
import dLib.ui.elements.items.resourcepicker.UIGlobalTextureResourcePicker;
import dLib.ui.elements.items.text.TextButton;
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
            valueBox = new TextButton(property.getValue().toString(), Dim.fill(), Dim.fill());
            valueBox.setTexture(Tex.stat(UICommonResources.button02_square));
            valueBox.onLeftClickEvent.subscribe(this, () -> {
                AbstractUIResourcePicker picker = new UIGlobalTextureResourcePicker();
                picker.onResourceSelectedEvent.subscribeManaged((aClass, s) -> property.setValue(new TextureResourceBinding(aClass, s)));
                picker.open();
            });
            contentBox.addChild(valueBox);
            contentBox.addChild(makeSwapComboBox());
        }

        property.onValueChangedEvent.subscribe(this, (textureBinding, textureBinding2) -> {
            if (!isEditorValidForPropertyChange()) return;

            valueBox.label.setText(textureBinding2.toString());
        });

        addChild(contentBox);
    }
}
