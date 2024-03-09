package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.settings.prefabs.StringProperty;

import java.util.function.Consumer;

public class StringSettingUI extends AbstractSettingUI {
    /** Variables */
    public StringSettingUI(StringProperty setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);

        middle = new Inputfield(setting.getValue(), (int)(xPos + width * (1-valuePercX)), valuePosY, (int)(width * valuePercX), valueHeight);
        ((Inputfield)(middle)).getButton().addOnSelectionStateChangedConsumer(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if(!aBoolean){
                    if(setting.getConfirmationMode().equals(StringProperty.InputConfirmationMode.SELECTION_MANAGED)) {
                        setting.setValue(((Inputfield) middle).getTextBox().getText());
                        if(!((Inputfield)(middle)).getTextBox().getText().equals(setting.getValue())){
                            ((Inputfield)(middle)).getTextBox().setText(setting.getValue());
                        }
                    }
                }
            }
        });
        ((Inputfield)(middle)).getTextBox().addOnTextChangedConsumer(new Consumer<String>() {
            @Override
            public void accept(String s) {
                if(setting.getConfirmationMode().equals(StringProperty.InputConfirmationMode.ON_TEXT_CHANGED)) setting.setValue(s);
            }
        });

        setting.addOnValueChangedListener(new Runnable() {
            @Override
            public void run() {
                Inputfield element = (Inputfield) middle;
                if(!element.getTextBox().getText().equals(setting.getValue())){
                    element.getTextBox().setText(setting.getValue());
                }
            }
        });
    }
}
