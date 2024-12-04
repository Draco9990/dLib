package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.Spacer;
import dLib.util.TextureManager;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.properties.objects.templates.TMethodBindingProperty;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class MethodBindingPropertyEditor extends CustomPropertyEditor<TMethodBindingProperty<?>, MethodBinding> {
    //region Variables

    Inputfield methodNameField;
    Button bindButton;
    Button resetButton;

    //endregion

    //region Constructors

    public MethodBindingPropertyEditor(TMethodBindingProperty setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TMethodBindingProperty property, AbstractDimension width, AbstractDimension height) {
        if(property.getValue() instanceof DynamicMethodBinding){
            return buildDynamicMethodPropertyEditor(property, width, height);
        }
        else {
            return super.buildContent(property, width, height);
        }
    }

    private UIElement buildDynamicMethodPropertyEditor(TMethodBindingProperty property, AbstractDimension width, AbstractDimension height){
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
