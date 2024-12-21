package dLib.util.bindings.texture.editors;

import dLib.properties.objects.TextureBindingProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.ComboBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.Reflection;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.bindings.texture.TextureResourceBinding;
import dLib.util.ui.dimensions.Dim;

import java.util.ArrayList;

public class TextureBindingValueEditor<ValueType> extends AbstractValueEditor<ValueType, TextureBindingProperty> {
    public TextureBindingValueEditor(TextureBindingProperty property) {
        super(property);
    }

    protected UIElement makeSwapComboBox(){
        ArrayList<Class<? extends TextureBinding>> methodBindingOptions = new ArrayList<>();
        methodBindingOptions.add(TextureNoneBinding.class);
        methodBindingOptions.add(TextureResourceBinding.class);

        ComboBox<Class<? extends TextureBinding>> comboBox = new ComboBox<Class<? extends TextureBinding>>(boundProperty.getValue().getClass(), methodBindingOptions, Dim.px(28), Dim.px(15)){
            @Override
            public String itemToStringShort(Class<? extends TextureBinding> item) {
                return Reflection.getFieldValue("PROPERTY_EDITOR_SHORT_NAME", item);
            }

            @Override
            public String itemToStringLong(Class<? extends TextureBinding> item) {
                return Reflection.getFieldValue("PROPERTY_EDITOR_LONG_NAME", item);
            }
        };
        comboBox.getTextBox().setFontScale(0.2f);

        comboBox.onSelectionChangedEvent.subscribe(comboBox, (aClass) -> {
            if(aClass == TextureNoneBinding.class){
                boundProperty.setValue(new TextureNoneBinding());
            }
            else if(aClass == TextureResourceBinding.class){
                boundProperty.setValue(new TextureResourceBinding(UICommonResources.class, "white_pixel"));
            }
        });

        return comboBox;
    }
}
