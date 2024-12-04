package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.TextureManager;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.properties.objects.templates.TMethodBindingProperty;

public class MethodBindingPropertyEditor extends CustomPropertyEditor<TMethodBindingProperty<?>, MethodBinding> {
    //region Variables

    Inputfield methodNameField;
    Button bindButton;
    Button resetButton;

    //endregion

    //region Constructors

    public MethodBindingPropertyEditor(TMethodBindingProperty setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);

        setting.addOnValueChangedListener((oldValue, newValue) -> {
            if(!oldValue.getClass().equals(newValue.getClass())){
                delayedActions.add(() -> buildElement(property, getWidthUnscaled(), originalHeight));
            }
        });
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TMethodBindingProperty property, Integer width, Integer height) {
        if(property.getValue() instanceof DynamicMethodBinding){
            return buildDynamicMethodPropertyEditor(property, width, height);
        }
        else {
            return super.buildContent(property, width, height);
        }
    }

    private UIElement buildDynamicMethodPropertyEditor(TMethodBindingProperty property, Integer width, Integer height){
        int buttonDim = Math.min(height, (int)(0.3 * width));

        DynamicMethodBinding dynamicMethodBinding = (DynamicMethodBinding) property.getValue();

        HorizontalBox elementBox = new HorizontalBox(0, 0, width, height);

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

        return elementBox;
    }

    @Override
    public boolean onRightInteraction() {
        bindButton.trigger();
        return true;
    }

    @Override
    public boolean onCancelInteraction() {
        resetButton.trigger();
        return true;
    }

    //endregion
}
