package dLib.util.bindings.method.editors;

import dLib.properties.objects.MethodBindingProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.ComboBox;
import dLib.util.Reflection;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.staticbindings.NoneMethodBinding;
import dLib.util.bindings.method.staticbindings.StaticMethodBinding;
import dLib.util.ui.dimensions.Dim;

import java.util.ArrayList;

public class MethodBindingValueEditor<ValueType> extends AbstractValueEditor<ValueType, MethodBindingProperty> {
    public MethodBindingValueEditor(MethodBindingProperty property) {
        super(property);
    }

    protected UIElement makeSwapComboBox(){
        ArrayList<Class<? extends MethodBinding>> methodBindingOptions = new ArrayList<>();
        methodBindingOptions.add(StaticMethodBinding.class);
        methodBindingOptions.add(DynamicMethodBinding.class);

        ComboBox<Class<? extends MethodBinding>> comboBox = new ComboBox<Class<? extends MethodBinding>>(boundProperty.getValue().getClass(), methodBindingOptions, Dim.px(28), Dim.px(15)){
            @Override
            public String itemToStringShort(Class<? extends MethodBinding> item) {
                return Reflection.getFieldValue("PROPERTY_EDITOR_SHORT_NAME", item);
            }

            @Override
            public String itemToStringLong(Class<? extends MethodBinding> item) {
                return Reflection.getFieldValue("PROPERTY_EDITOR_LONG_NAME", item);
            }
        };
        comboBox.getTextBox().setFontScale(0.2f);

        comboBox.addOnSelectedItemChangedEvent((classComboBox, aClass) -> {
            if(aClass == StaticMethodBinding.class){
                boundProperty.setValue(new NoneMethodBinding());
            }
            else if(aClass == DynamicMethodBinding.class){
                boundProperty.setValue(new DynamicMethodBinding(""));
            }
        });

        return comboBox;
    }
}
