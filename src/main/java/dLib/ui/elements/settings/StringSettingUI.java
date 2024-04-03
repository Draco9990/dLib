package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.settings.prefabs.StringProperty;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StringSettingUI extends AbstractSettingUI {
    //region Variables

    Inputfield input;

    //endregion

    //region Constructors

    public StringSettingUI(StringProperty setting, Integer xPos, Integer yPos, Integer width, Integer height, StringProperty.EInputConfirmationMode inputConfirmationMode){
        super(setting, xPos, yPos, width, height);

        input = new Inputfield(setting.getValue(), (int)(xPos + width * (1-valuePercX)), valuePosY, (int)(width * valuePercX), valueHeight);
        input.addOnValueChangedListener(s -> {
            if(inputConfirmationMode == StringProperty.EInputConfirmationMode.ON_TEXT_CHANGED){
                setting.setValue(s);
            }
        });
        input.addOnValueCommittedListener(s -> {
            if(inputConfirmationMode == StringProperty.EInputConfirmationMode.SELECTION_MANAGED){
                setting.setValue(input.getTextBox().getText());
                if(!input.getTextBox().getText().equals(setting.getValue())){
                    input.getTextBox().setText(setting.getValue());
                }
            }
        });
        addChildCS(input);

        setting.addOnValueChangedListener(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                if(!input.getTextBox().getText().equals(setting.getValue())){
                    input.getTextBox().setText(setting.getValue());
                }
            }
        });
    }

    //endregion

    //region Methods
    //endregion
}
