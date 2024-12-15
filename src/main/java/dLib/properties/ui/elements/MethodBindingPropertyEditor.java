package dLib.properties.ui.elements;

import dLib.properties.objects.templates.TProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.*;
import dLib.ui.themes.UIThemeManager;
import dLib.util.Reflection;
import dLib.util.TextureManager;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.properties.objects.templates.TMethodBindingProperty;
import dLib.util.bindings.method.staticbindings.NoneMethodBinding;
import dLib.util.bindings.method.staticbindings.StaticMethodBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class MethodBindingPropertyEditor extends AbstractPropertyEditor<TMethodBindingProperty<? extends TMethodBindingProperty>> {
    //region Variables

    UIElement propertyEditorElement;

    Button bindDynamicBindingButton;

    VerticalBox staticBindingPropertiesBox;

    //endregion

    //region Constructors

    public MethodBindingPropertyEditor(TMethodBindingProperty<? extends TMethodBindingProperty> setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline){
        super(setting, xPos, yPos, width, multiline);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TMethodBindingProperty<? extends TMethodBindingProperty> property, AbstractDimension width, AbstractDimension height) {
        VerticalBox mainVerticalBox = new VerticalBox(Dim.fill(), Dim.auto());
        {
            ArrayList<Class<? extends MethodBinding>> methodBindingOptions = new ArrayList<>();
            methodBindingOptions.add(StaticMethodBinding.class);
            methodBindingOptions.add(DynamicMethodBinding.class);

            HorizontalBox mainBox = new HorizontalBox(width, Dim.px(50));
            {
                mainBox.addItem(buildPropertyEditorForBinding(property));

                VerticalBox methodBindingOptionsBox = new VerticalBox(Dim.px(28), Dim.fill());
                {
                    VerticalBox extraOptionsBox = new VerticalBox(Dim.fill(), Dim.fill());
                    {
                        extraOptionsBox.addItem(bindDynamicBindingButton = new Button(Dim.fill(), Dim.px(15)));
                        bindDynamicBindingButton.setImage(TextureManager.getTexture("dLibResources/images/ui/uieditor/BindButton.png"));
                        bindDynamicBindingButton.hideAndDisableInstantly();
                        bindDynamicBindingButton.onLeftClickEvent.subscribeManaged(() -> {
                            if(property.getValue() instanceof DynamicMethodBinding){
                                ((DynamicMethodBinding) property.getValue()).setBoundMethod(property.getDynamicCreationDefaultMethodName());
                                property.createDynamicMethod();
                            }
                            bindDynamicBindingButton.hideAndDisableInstantly();
                        });
                    }
                    methodBindingOptionsBox.addItem(extraOptionsBox);

                    ComboBox<Class<? extends MethodBinding>> methodBindingType = new ComboBox<Class<? extends MethodBinding>>(StaticMethodBinding.class, methodBindingOptions, Dim.px(28), Dim.px(15)){
                        @Override
                        public String itemToStringShort(Class<? extends MethodBinding> item) {
                            return Reflection.getFieldValue("PROPERTY_EDITOR_SHORT_NAME", item);
                        }

                        @Override
                        public String itemToStringLong(Class<? extends MethodBinding> item) {
                            return Reflection.getFieldValue("PROPERTY_EDITOR_LONG_NAME", item);
                        }
                    };
                    methodBindingType.addOnSelectedItemChangedEvent((classComboBox, aClass) -> {
                        if(aClass == StaticMethodBinding.class){
                            property.setValue(new NoneMethodBinding());
                        }
                        else if(aClass == DynamicMethodBinding.class){
                            property.setValue(new DynamicMethodBinding(""));
                            bindDynamicBindingButton.showAndEnableInstantly();
                        }
                    });
                    methodBindingOptionsBox.addItem(methodBindingType);
                }
                mainBox.addItem(methodBindingOptionsBox);
            }
            mainVerticalBox.addItem(mainBox);

            staticBindingPropertiesBox = new VerticalBox(Dim.fill(), Dim.auto());
            {
                buildStaticBindingPropertiesBox(property);
            }
            staticBindingPropertiesBox.setPaddingLeft(Padd.px(20));
            mainVerticalBox.addItem(staticBindingPropertiesBox);
        }

        property.onValueChangedEvent.subscribe(this, (methodBinding, methodBinding2) -> {
            delayedActions.add(() -> {
                staticBindingPropertiesBox.clearItems();

                if(methodBinding instanceof StaticMethodBinding && methodBinding2 instanceof DynamicMethodBinding ||
                        methodBinding instanceof DynamicMethodBinding && methodBinding2 instanceof StaticMethodBinding){
                    buildPropertyEditorForBinding(property);
                }

                if(methodBinding2 instanceof StaticMethodBinding){
                    buildStaticBindingPropertiesBox(property);
                }
            });
        });

        return mainVerticalBox;
    }

    private UIElement buildPropertyEditorForBinding(TMethodBindingProperty<? extends TMethodBindingProperty> property){
        UIElement createdElement = null;
        if(property.getValue() instanceof StaticMethodBinding){
            ComboBox<Class<? extends StaticMethodBinding>> methodBindingType = new ComboBox<Class<? extends StaticMethodBinding>>(
                    NoneMethodBinding.class,
                    Reflection.findClassesOfType(StaticMethodBinding.class, false),
                    Dim.fill(),
                    Dim.fill()){
                @Override
                public String itemToString(Class<? extends StaticMethodBinding> item) {
                    return Reflection.getFieldValue("PROPERTY_EDITOR_LONG_NAME", item);
                }
            };
            methodBindingType.addOnSelectedItemChangedEvent((classComboBox, aClass) -> {
                try{
                    StaticMethodBinding newBinding = aClass.newInstance();
                    property.setValue(newBinding);
                }
                catch (InstantiationException | IllegalAccessException e){
                    e.printStackTrace();
                }
            });

            createdElement = methodBindingType;

            property.onValueChangedEvent.subscribe(this, (methodBinding, methodBinding2) -> {
                if(methodBinding instanceof StaticMethodBinding && methodBinding2 instanceof StaticMethodBinding){
                    ((TextButton) propertyEditorElement).getTextBox().setText(methodBinding2.getDisplayValue());
                }
            });
        }
        else if(property.getValue() instanceof DynamicMethodBinding){
            createdElement = new Inputfield(((DynamicMethodBinding) property.getValue()).getBoundMethod(), Dim.fill(), Dim.fill());
            ((Inputfield)createdElement).getTextBox().addOnTextChangedConsumer(s -> {
                ((DynamicMethodBinding) property.getValue()).setBoundMethod(s);
            });

            ((DynamicMethodBinding) property.getValue()).getBoundMethodRaw().onValueChangedEvent.subscribe(this, (methodBinding, methodBinding2) -> {
                ((Inputfield) propertyEditorElement).getTextBox().setText(methodBinding2);
            });
        }

        if(createdElement == null){
            return new Spacer(Dim.fill(), Dim.fill());
        }

        if(propertyEditorElement != null){
            propertyEditorElement.getParent().replaceChild(propertyEditorElement, createdElement);
        }

        propertyEditorElement = createdElement;

        return propertyEditorElement;
    }

    private void buildStaticBindingPropertiesBox(TMethodBindingProperty<? extends TMethodBindingProperty> property){
        staticBindingPropertiesBox.clearItems();

        if(!(property.getValue() instanceof StaticMethodBinding)){
            return;
        }

        for(TProperty<?, ?> param : ((StaticMethodBinding)property.getValue()).getDeclaredParams()){
            staticBindingPropertiesBox.addItem(param.makePropertyEditor(Pos.px(0), Pos.px(0), Dim.fill(), true));
        }
    }

    //endregion
}
