package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.TextureManager;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.util.settings.prefabs.MethodBindingProperty;

import java.util.function.Consumer;

public class DynamicMethodSettingUI extends AbstractSettingUI {
    /** Variables */
    public DynamicMethodSettingUI(MethodBindingProperty setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);

        int buttonDim = Math.min(valueHeight, (int)((valuePercX - 0.3) * width));

        DynamicMethodBinding dynamicMethodBinding = (DynamicMethodBinding) setting.getValue();

        middle = new Inputfield(dynamicMethodBinding.getBoundMethod(), (int)(xPos + width * (1-valuePercX)), valuePosY, (int)(width * valuePercX) - buttonDim * 2, valueHeight);
        ((Inputfield)(middle)).getButton().addOnSelectionStateChangedConsumer(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean selected) {
                if(!selected){
                    dynamicMethodBinding.setBoundMethod(((Inputfield)middle).getTextBox().getText());
                    if(!dynamicMethodBinding.getBoundMethod().equals(((Inputfield)middle).getTextBox().getText())){
                        ((Inputfield)middle).getTextBox().setText(dynamicMethodBinding.getBoundMethod());
                    }
                }
            }
        });

        foreground.add(new Button((int)(xPos + middle.getWidth()), valuePosY, buttonDim, buttonDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                if(((Inputfield) middle).getTextBox().getText().isEmpty()){
                    ((Inputfield)middle).getTextBox().setText(setting.getPreferredMethodName());
                    dynamicMethodBinding.setBoundMethod(((Inputfield)middle).getTextBox().getText());
                    if(!dynamicMethodBinding.getBoundMethod().equals(((Inputfield)middle).getTextBox().getText())){
                        ((Inputfield)middle).getTextBox().setText(dynamicMethodBinding.getBoundMethod());
                    }
                }
            }
        }.setImage(TextureManager.getTexture("dLibResources/images/ui/screeneditor/BindButton.png")));
        foreground.add(new Button(xPos + middle.getWidth() + buttonDim, valuePosY, buttonDim, buttonDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                setting.setValue(new NoneMethodBinding());
            }
        }.setImage(TextureManager.getTexture("dLibResources/images/ui/screeneditor/ResetButton.png")));

        dynamicMethodBinding.getBoundMethodSetting().addOnValueChangedListener(new Runnable() {
            @Override
            public void run() {
                Inputfield element = (Inputfield) middle;
                if(!element.getTextBox().getText().equals(dynamicMethodBinding.getBoundMethod())){
                    element.getTextBox().setText(dynamicMethodBinding.getBoundMethod());
                }
            }
        });
    }
}
