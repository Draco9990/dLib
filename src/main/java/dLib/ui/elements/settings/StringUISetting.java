package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.Toggle;
import dLib.util.settings.prefabs.StringSetting;

import java.util.function.Consumer;

public class StringUISetting extends AbstractUISetting {
    /** Variables */
    public StringUISetting(StringSetting setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);

        middle = new Inputfield(setting.getCurrentValue(), (int)(xPos + width * textPerc), yPos, (int)(width * (1-textPerc)), height);
        ((Inputfield)(middle)).getTextBox().setOnTextChangedConsumer(new Consumer<String>() {
            @Override
            public void accept(String s) {
                setting.setCurrentValue(s);
            }
        });

        setting.setOnValueChangedConsumer(new Runnable() {
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
