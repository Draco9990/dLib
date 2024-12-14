package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.*;
import dLib.ui.themes.UIThemeManager;
import dLib.util.Reflection;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.properties.objects.templates.TMethodBindingProperty;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.util.bindings.method.StaticMethodBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import org.apache.logging.log4j.util.BiConsumer;

import java.util.ArrayList;

public class MethodBindingPropertyEditor extends AbstractPropertyEditor<TMethodBindingProperty<? extends TMethodBindingProperty>> {
    //region Variables

    UIElement propertyEditorElement;

    //endregion

    //region Constructors

    public MethodBindingPropertyEditor(TMethodBindingProperty<? extends TMethodBindingProperty> setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline){
        super(setting, xPos, yPos, width, multiline);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TMethodBindingProperty<? extends TMethodBindingProperty> property, AbstractDimension width, AbstractDimension height) {
        ArrayList<Class<? extends MethodBinding>> methodBindingOptions = new ArrayList<>();
        methodBindingOptions.add(StaticMethodBinding.class);
        methodBindingOptions.add(DynamicMethodBinding.class);

        HorizontalBox mainBox = new HorizontalBox(width, height);
        {
            mainBox.addItem(buildPropertyEditorForBinding(property));

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
            mainBox.addItem(methodBindingType);
        }

        property.onValueChangedEvent.subscribe(this, (methodBinding, methodBinding2) -> {
            delayedActions.add(() -> {
                if(methodBinding instanceof StaticMethodBinding && methodBinding2 instanceof DynamicMethodBinding ||
                        methodBinding instanceof DynamicMethodBinding && methodBinding2 instanceof StaticMethodBinding){
                    buildPropertyEditorForBinding(property);
                }
            });
        });

        return mainBox;
    }

    private UIElement buildPropertyEditorForBinding(TMethodBindingProperty<? extends TMethodBindingProperty> property){
        UIElement createdElement = null;
        if(property.getValue() instanceof StaticMethodBinding){
            createdElement = new TextButton(property.getValue().getFullDisplayName(), Dim.fill(), Dim.fill());
            ((TextButton)createdElement).getButton().setImage(UIThemeManager.getDefaultTheme().button_large_square);

            property.onValueChangedEvent.subscribe(this, (methodBinding, methodBinding2) -> {
                if(methodBinding instanceof StaticMethodBinding && methodBinding2 instanceof StaticMethodBinding){
                    ((TextButton) propertyEditorElement).getTextBox().setText(methodBinding2.getFullDisplayName());
                }
            });
        }
        else if(property.getValue() instanceof DynamicMethodBinding){
            createdElement = new Inputfield(((DynamicMethodBinding) property.getValue()).getBoundMethod(), Dim.fill(), Dim.fill());

            property.onValueChangedEvent.subscribe(this, (methodBinding, methodBinding2) -> {
                if(methodBinding instanceof DynamicMethodBinding && methodBinding2 instanceof DynamicMethodBinding){
                    ((Inputfield) propertyEditorElement).getTextBox().setText(((DynamicMethodBinding) methodBinding2).getBoundMethod());
                }
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

    private UIElement buildDynamicMethodPropertyEditor(TMethodBindingProperty<? extends TMethodBindingProperty> property, AbstractDimension width, AbstractDimension height){
        /*int buttonDim = Math.min(height, (int)(0.3 * width));

        DynamicMethodBinding dynamicMethodBinding = (DynamicMethodBinding) property.getValue();

        HorizontalBox elementBox = new HorizontalBox(Pos.px(0), Pos.px(0), width, height);

        methodNameField = new Inputfield(dynamicMethodBinding.getBoundMethod(), 0, 0, width - buttonDim * 2, height);
        methodNameField.getButton().addOnSelectionStateChangedConsumer(selected -> {
            if(!selected){
                dynamicMethodBinding.setBoundMethod(methodNameField.getTextBox().getText());
                if(!dynamicMethodBinding.getBoundMethod().equals(methodNameField.getTextBox().getText())){
                    methodNameField.getTextBox().setText(dynamicMethodBinding.getBoundMethod());
                }
            }
        });
        elementBox.addItem(methodNameField);

        bindButton = new Button(0, 0, buttonDim, buttonDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                if(methodNameField.getTextBox().getText().isEmpty()){
                    methodNameField.getTextBox().setText(property.getDNCMethodName());
                    dynamicMethodBinding.setBoundMethod(methodNameField.getTextBox().getText());
                    if(!dynamicMethodBinding.getBoundMethod().equals(methodNameField.getTextBox().getText())){
                        methodNameField.getTextBox().setText(dynamicMethodBinding.getBoundMethod());
                    }
                }
            }
        }.setImage(TextureManager.getTexture("dLibResources/images/ui/screeneditor/BindButton.png"));
        elementBox.addItem(bindButton);

        resetButton = new Button(0, 0, buttonDim, buttonDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                property.setValue(new NoneMethodBinding());
            }
        }.setImage(TextureManager.getTexture("dLibResources/images/ui/screeneditor/ResetButton.png"));
        elementBox.addItem(resetButton);

        dynamicMethodBinding.getBoundMethodSetting().addOnValueChangedListener((s, s2) -> {
            if(!methodNameField.getTextBox().getText().equals(dynamicMethodBinding.getBoundMethod())){
                methodNameField.getTextBox().setText(dynamicMethodBinding.getBoundMethod());
            }
        });

        return elementBox;*/
        return new Spacer(Dim.fill(), Dim.fill());
    }

    //endregion
}
