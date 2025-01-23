package dLib.util.bindings.font.editors;

import dLib.properties.objects.FontBindingProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.elements.items.resourcepicker.AbstractUIResourcePicker;
import dLib.ui.elements.items.resourcepicker.UIGlobalFontResourcePicker;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.font.FontResourceBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

public class FontResourceBindingValueEditor extends AbstractValueEditor<FontResourceBinding, FontBindingProperty> {
    private TextButton valueBox;

    public FontResourceBindingValueEditor(FontResourceBinding binding) {
        this(new FontBindingProperty(binding));
    }

    public FontResourceBindingValueEditor(FontBindingProperty property) {
        super(property);

        valueBox = new TextButton(property.getValue().getDisplayValue(), Dim.fill(), Dim.px(50));
        valueBox.setImage(Tex.stat(UICommonResources.button02_square));
        valueBox.onLeftClickEvent.subscribe(this, () -> {
            AbstractUIResourcePicker picker = new UIGlobalFontResourcePicker();
            picker.onResourceSelectedEvent.subscribeManaged((aClass, s) -> property.setValue(new FontResourceBinding(aClass, s)));
            picker.open();
        });
        addChild(valueBox);

        property.onValueChangedEvent.subscribe(this, (textureBinding, textureBinding2) -> {
            if (!isEditorValidForPropertyChange()) return;

            valueBox.label.setText(textureBinding2.getDisplayValue());
        });
    }
}
