package dLib.util.bindings.string.editors;

import dLib.properties.objects.StringBindingProperty;
import dLib.properties.objects.TextureBindingProperty;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.string.StringStaticBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.bindings.texture.editors.TextureBindingValueEditor;
import dLib.util.ui.dimensions.Dim;

public class StringStaticBindingValueEditor extends StringBindingValueEditor<StringStaticBinding> {

    protected Inputfield inputfield;

    public StringStaticBindingValueEditor(StringStaticBinding binding) {
        this(new StringBindingProperty(binding));
    }

    public StringStaticBindingValueEditor(StringBindingProperty property) {
        super(property);

        inputfield = new Inputfield(property.getValueForDisplay(), Dim.fill(), Dim.px(50));
        inputfield.onValueCommittedEvent.subscribeManaged(s -> boundProperty.setValue(new StringStaticBinding(s)));

        property.onValueChangedEvent.subscribe(this, (oldVal, newValue) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputfield.textBox.getText().equals(newValue.getBoundObject())){
                inputfield.textBox.setText(newValue.getBoundObject());
            }
        });

        addChild(inputfield);
    }
}
