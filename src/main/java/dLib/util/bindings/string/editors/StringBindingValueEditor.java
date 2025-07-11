package dLib.util.bindings.string.editors;

import dLib.properties.objects.StringBindingProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.ComboBox;
import dLib.util.Reflection;
import dLib.util.bindings.string.AbstractStringBinding;
import dLib.util.bindings.string.StringStaticBinding;
import dLib.util.ui.dimensions.Dim;

import java.util.ArrayList;

public class StringBindingValueEditor<ValueType> extends AbstractValueEditor<ValueType, StringBindingProperty> {
    public StringBindingValueEditor(StringBindingProperty property) {
        super(property);
    }

    protected UIElement makeSwapComboBox(){
        ArrayList<Class<? extends AbstractStringBinding>> methodBindingOptions = new ArrayList<>();
        methodBindingOptions.add(StringStaticBinding.class);

        ComboBox<Class<? extends AbstractStringBinding>> comboBox = new ComboBox<Class<? extends AbstractStringBinding>>(boundProperty.getValue().getClass(), methodBindingOptions, Dim.px(28), Dim.px(15)){
            @Override
            public String itemToStringShort(Class<? extends AbstractStringBinding> item) {
                return Reflection.getFieldValue("PROPERTY_EDITOR_SHORT_NAME", item);
            }

            @Override
            public String itemToStringLong(Class<? extends AbstractStringBinding> item) {
                return Reflection.getFieldValue("PROPERTY_EDITOR_LONG_NAME", item);
            }
        };
        comboBox.label.setFontSize(6);

        comboBox.onSelectionChangedEvent.subscribe(comboBox, (aClass) -> {
            if(aClass == StringStaticBinding.class){
                boundProperty.setValue(new StringStaticBinding(""));
            }
        });

        return comboBox;
    }
}
