package dLib.util.bindings.method.editors;

import dLib.properties.objects.MethodBindingProperty;
import dLib.properties.objects.templates.TMethodBindingProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.ValueEditorManager;
import dLib.ui.elements.prefabs.*;
import dLib.util.Reflection;
import dLib.util.bindings.method.staticbindings.NoneMethodBinding;
import dLib.util.bindings.method.staticbindings.StaticMethodBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

public class StaticMethodBindingValueEditor extends MethodBindingValueEditor<StaticMethodBinding> {

    private ComboBox<Class<? extends StaticMethodBinding>> selectedMethodBinding;
    private VerticalBox methodBindingArgumentsBox;

    public StaticMethodBindingValueEditor(StaticMethodBinding value, AbstractDimension width, AbstractDimension height) {
        this(new MethodBindingProperty(value), width, height);
    }

    public StaticMethodBindingValueEditor(MethodBindingProperty property, AbstractDimension width, AbstractDimension height) {
        super(property, width, height);

        VerticalBox mainContentBox = new VerticalBox(Dim.fill(), Dim.auto());
        {
            HorizontalBox propertyValueBox = new HorizontalBox(width, Dim.px(50));
            {
                selectedMethodBinding = new ComboBox<Class<? extends StaticMethodBinding>>(
                        NoneMethodBinding.class,
                        Reflection.findClassesOfType(StaticMethodBinding.class, false),
                        Dim.fill(),
                        Dim.fill()){
                    @Override
                    public String itemToString(Class<? extends StaticMethodBinding> item) {
                        return Reflection.getFieldValue("PROPERTY_EDITOR_LONG_NAME", item);
                    }
                };
                selectedMethodBinding.addOnSelectedItemChangedEvent((classComboBox, aClass) -> {
                    try{
                        StaticMethodBinding newBinding = aClass.newInstance();
                        property.setValue(newBinding);
                    }
                    catch (InstantiationException | IllegalAccessException e){
                        e.printStackTrace();
                    }
                });
                propertyValueBox.addItem(selectedMethodBinding);

                VerticalBox methodBindingOptionsBox = new VerticalBox(Dim.px(28), Dim.fill());
                {
                    methodBindingOptionsBox.addItem(new Spacer(Dim.fill(), Dim.fill()));
                    methodBindingOptionsBox.addItem(makeSwapComboBox());
                }
                propertyValueBox.addItem(methodBindingOptionsBox);
            }
            mainContentBox.addItem(propertyValueBox);

            methodBindingArgumentsBox = new VerticalBox(Dim.fill(), Dim.auto());
            {
                buildStaticBindingPropertiesBox(property);
            }
            methodBindingArgumentsBox.setPaddingLeft(Padd.px(20));
            mainContentBox.addItem(methodBindingArgumentsBox);
        }

        property.onValueChangedEvent.subscribe(this, (methodBinding, methodBinding2) -> {
            if(!isEditorValidForPropertyChange()) return;

            selectedMethodBinding.setSelectedItem((Class<? extends StaticMethodBinding>) methodBinding2.getClass());
            buildStaticBindingPropertiesBox(property);
        });

        addChildNCS(mainContentBox);
    }

    private void buildStaticBindingPropertiesBox(TMethodBindingProperty<? extends TMethodBindingProperty> property){
        methodBindingArgumentsBox.clearItems();

        for(TProperty<?, ?> param : ((StaticMethodBinding)property.getValue()).getDeclaredParams()){
            methodBindingArgumentsBox.addItem(ValueEditorManager.makeEditorFor(param, Dim.fill(), Dim.px(50)));
        }
    }
}
