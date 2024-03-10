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

    public StringSettingUI(StringProperty setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);

        input = new Inputfield(setting.getValue(), (int)(xPos + width * (1-valuePercX)), valuePosY, (int)(width * valuePercX), valueHeight);
        input.getButton().addOnSelectionStateChangedConsumer(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if(!aBoolean){
                    if(setting.getConfirmationMode().equals(StringProperty.InputConfirmationMode.SELECTION_MANAGED)) {
                        setting.setValue(input.getTextBox().getText());
                        if(!input.getTextBox().getText().equals(setting.getValue())){
                            input.getTextBox().setText(setting.getValue());
                        }
                    }
                }
            }
        });
        input.getTextBox().addOnTextChangedConsumer(new Consumer<String>() {
            @Override
            public void accept(String s) {
                if(setting.getConfirmationMode().equals(StringProperty.InputConfirmationMode.ON_TEXT_CHANGED)) setting.setValue(s);
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
