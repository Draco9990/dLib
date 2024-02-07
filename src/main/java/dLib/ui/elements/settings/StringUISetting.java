package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.settings.prefabs.StringSetting;

import java.util.function.Consumer;

public class StringUISetting extends AbstractUISetting {
    /** Variables */
    public StringUISetting(StringSetting setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);

        middle = new Inputfield(setting.getCurrentValue(), (int)(xPos + width * 0.75f), yPos, (int)(width * 0.25f), height);
        ((Inputfield)(middle)).getTextBox().setOnTextChangedConsumer(new Consumer<String>() {
            @Override
            public void accept(String s) {
                setting.setCurrentValue(s);
            }
        });
    }
}
