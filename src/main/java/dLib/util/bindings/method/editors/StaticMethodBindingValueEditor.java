package dLib.util.bindings.method.editors;

import dLib.properties.objects.MethodBindingProperty;
import dLib.properties.objects.templates.TMethodBindingProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.ui.elements.items.*;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.util.Reflection;
import dLib.util.bindings.method.staticbindings.StaticMethodBinding;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;

public class StaticMethodBindingValueEditor extends MethodBindingValueEditor<StaticMethodBinding> {

    private ComboBox<Class<? extends StaticMethodBinding>> selectedMethodBinding;
    private VerticalBox methodBindingArgumentsBox;

    public StaticMethodBindingValueEditor(StaticMethodBinding value) {
        this(new MethodBindingProperty(value));
    }

    public StaticMethodBindingValueEditor(MethodBindingProperty property) {
        super(property);

        VerticalBox mainContentBox = new VerticalBox(Dim.fill(), Dim.auto());
        {
            HorizontalBox propertyValueBox = new HorizontalBox(Dim.fill(), Dim.px(50));
            {
                selectedMethodBinding = new ComboBox<Class<? extends StaticMethodBinding>>(
                        (Class<? extends StaticMethodBinding>) property.getValue().getClass(),
                        Reflection.findClassesOfType(StaticMethodBinding.class, false),
                        Dim.fill(),
                        Dim.fill()){
                    @Override
                    public String itemToString(Class<? extends StaticMethodBinding> item) {
                        return Reflection.getFieldValue("PROPERTY_EDITOR_LONG_NAME", item);
                    }
                };
                selectedMethodBinding.onSelectionChangedEvent.subscribe(selectedMethodBinding, (aClass) -> {
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
            if(!param.isVisible()) continue;

            methodBindingArgumentsBox.addItem(param.makeEditorFor(true));

            param.onValueChangedEvent.subscribe(this, (property1, property2) -> {
                buildStaticBindingPropertiesBox(boundProperty);
            });
        }
    }
}
