package dLib.ui.elements.settings;

import dLib.plugin.intellij.PluginManager;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.TextureManager;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.util.settings.prefabs.MethodBindingSetting;

import java.util.UUID;
import java.util.function.Consumer;

public class DynamicMethodSettingUI extends AbstractSettingUI {
    /** Variables */
    public DynamicMethodSettingUI(MethodBindingSetting setting, Integer xPos, Integer yPos, Integer width, Integer height){
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

        foreground.add(new Button((int)(xPos + middle.getWidth()), valuePosY, buttonDim, buttonDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                if(((Inputfield) middle).getTextBox().getText().isEmpty()){
                    ((Inputfield)middle).getTextBox().setText("MethodBinding_" + UUID.randomUUID().toString().replace("-", ""));
                }
            }
        }.setImage(TextureManager.getTexture("dLibResources/images/ui/screeneditor/BindButton.png")));
        foreground.add(new Button(xPos + middle.getWidth() + buttonDim, valuePosY, buttonDim, buttonDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                setting.trySetValue(new NoneMethodBinding());
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
