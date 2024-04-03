package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.TextureManager;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.util.settings.prefabs.MethodBindingProperty;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DynamicMethodSettingUI extends AbstractSettingUI {
    //region Variables

    Inputfield methodNameField;
    Button bindButton;
    Button resetButton;

    //endregion

    //region Constructors

    public DynamicMethodSettingUI(MethodBindingProperty setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);

        int buttonDim = Math.min(valueHeight, (int)((valuePercX - 0.3) * width));

        DynamicMethodBinding dynamicMethodBinding = (DynamicMethodBinding) setting.getValue();

        HorizontalBox elementBox = new HorizontalBox(0, 0, (int) (width * valuePercX), valueHeight);

        methodNameField = new Inputfield(dynamicMethodBinding.getBoundMethod(), 0, 0, (int)(width * valuePercX) - buttonDim * 2, valueHeight);
        methodNameField.getButton().addOnSelectionStateChangedConsumer(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean selected) {
                if(!selected){
                    dynamicMethodBinding.setBoundMethod(methodNameField.getTextBox().getText());
                    if(!dynamicMethodBinding.getBoundMethod().equals(methodNameField.getTextBox().getText())){
                        methodNameField.getTextBox().setText(dynamicMethodBinding.getBoundMethod());
                    }
                }
            }
        });
        elementBox.addItem(methodNameField);

        bindButton = new Button(0, 0, buttonDim, buttonDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                if(methodNameField.getTextBox().getText().isEmpty()){
                    methodNameField.getTextBox().setText(setting.getDNCMethodName());
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
                setting.setValue(new NoneMethodBinding());
            }
        }.setImage(TextureManager.getTexture("dLibResources/images/ui/screeneditor/ResetButton.png"));
        elementBox.addItem(resetButton);

        addChildCS(elementBox);

        dynamicMethodBinding.getBoundMethodSetting().addOnValueChangedListener((s, s2) -> {
            if(!methodNameField.getTextBox().getText().equals(dynamicMethodBinding.getBoundMethod())){
                methodNameField.getTextBox().setText(dynamicMethodBinding.getBoundMethod());
            }
        });
    }

    //endregion

    //region Methods

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
