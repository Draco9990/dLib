package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.TextureManager;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.util.settings.prefabs.MethodSetting;
import dLib.util.settings.prefabs.StringSetting;

import java.util.function.Consumer;

public class DynamicMethodSettingUI extends AbstractSettingUI {
    /** Variables */
    public DynamicMethodSettingUI(MethodSetting setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);

        int buttonDim = Math.min(valueHeight, (int)((valuePercX - 0.3) * width));

        DynamicMethodBinding dynamicMethodBinding = (DynamicMethodBinding) setting.getCurrentValue();

        middle = new Inputfield(dynamicMethodBinding.getBoundMethod(), (int)(xPos + width * (1-valuePercX)), valuePosY, (int)(width * valuePercX) - buttonDim * 2, valueHeight);
        ((Inputfield)(middle)).getTextBox().setOnTextChangedConsumer(new Consumer<String>() {
            @Override
            public void accept(String s) {
                dynamicMethodBinding.setBoundMethod(s);
            }
        });

        other.add(new Button((int)(xPos + middle.getWidth()), valuePosY, buttonDim, buttonDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                //TODO
            }
        }.setImage(TextureManager.getTexture("dLibResources/images/ui/screeneditor/BindButton.png")));
        other.add(new Button(xPos + middle.getWidth() + buttonDim, valuePosY, buttonDim, buttonDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                setting.setCurrentValue(new NoneMethodBinding());
            }
        }.setImage(TextureManager.getTexture("dLibResources/images/ui/screeneditor/ResetButton.png")));

        setting.setOnValueChangedConsumer(new Runnable() {
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
