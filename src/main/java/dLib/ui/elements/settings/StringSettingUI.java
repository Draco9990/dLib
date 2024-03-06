package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.settings.prefabs.StringSetting;

import java.util.function.Consumer;

public class StringSettingUI extends AbstractSettingUI {
    /** Variables */
    public StringSettingUI(StringSetting setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);

        middle = new Inputfield(setting.getCurrentValue(), (int)(xPos + width * (1-valuePercX)), valuePosY, (int)(width * valuePercX), valueHeight);
        ((Inputfield)(middle)).getButton().addOnDeselectedConsumer(new Runnable() {
            @Override
            public void run() {
                if(setting.getConfirmationMode().equals(StringSetting.InputConfirmationMode.SELECTION_MANAGED)) {
                    setting.trySetValue(((Inputfield) middle).getTextBox().getText());
                    if(!((Inputfield)(middle)).getTextBox().getText().equals(setting.getCurrentValue())){
                        ((Inputfield)(middle)).getTextBox().setText(setting.getCurrentValue());
                    }
                }
            }
        });
        ((Inputfield)(middle)).getTextBox().addOnTextChangedConsumer(new Consumer<String>() {
            @Override
            public void accept(String s) {
                if(setting.getConfirmationMode().equals(StringSetting.InputConfirmationMode.ON_TEXT_CHANGED)) setting.trySetValue(s);
            }
        });

        setting.addOnValueChangedConsumer(new Runnable() {
            @Override
            public void run() {
                Inputfield element = (Inputfield) middle;
                if(!element.getTextBox().getText().equals(setting.getCurrentValue())){
                    element.getTextBox().setText(setting.getCurrentValue());
                }
            }
        });
    }
}
