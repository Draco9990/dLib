package dLib.util.bindings.font.editors;

import dLib.properties.objects.FontBindingProperty;
import dLib.properties.objects.TextureBindingProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.elements.items.UIResourcePicker;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.font.fontresource.FontResourceBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.TextureResourceBinding;
import dLib.util.bindings.texture.editors.TextureBindingValueEditor;
import dLib.util.ui.dimensions.Dim;

public class FontResourceBindingValueEditor extends AbstractValueEditor<FontResourceBinding, FontBindingProperty> {
    private TextButton valueBox;

    public FontResourceBindingValueEditor(FontResourceBinding binding) {
        this(new FontBindingProperty(binding));
    }

    public FontResourceBindingValueEditor(FontBindingProperty property) {
        super(property);

        valueBox = new TextButton(property.getValue().getDisplayValue(), Dim.fill(), Dim.fill());
        valueBox.setImage(Tex.stat(UICommonResources.button02_horizontal));
        valueBox.onLeftClickEvent.subscribe(this, () -> {
            UIResourcePicker picker = new UIResourcePicker(); //rename resource picker to an abstract class and add UITextureResourcePicker or something
            //picker.onResourceSelectedEvent.subscribeManaged((aClass, s) -> property.setValue(new TextureResourceBinding(aClass, s)));
            picker.open();
        });
        addChildNCS(valueBox);

        property.onValueChangedEvent.subscribe(this, (textureBinding, textureBinding2) -> {
            if (!isEditorValidForPropertyChange()) return;

            valueBox.label.setText(textureBinding2.getDisplayValue());
        });
    }
}
