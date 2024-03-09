package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.themes.UIThemeManager;
import dLib.util.settings.prefabs.IntegerSetting;

import java.util.function.Consumer;

public class IntegerSettingUI extends AbstractSettingUI {
    /** Constructors */
    public IntegerSettingUI(IntegerSetting setting, Integer xPos, Integer yPos, Integer width, int height, boolean showArrows){
        super(setting, xPos, yPos, width, height);

        int startingX = (int) (xPos + width * (1-valuePercX));

        if(showArrows){
            int arrowDim = Math.min((int)(arrowPercX * width), valueHeight);

            int hOffset = 0;
            if(arrowDim != height){
                hOffset = (int)((height-arrowDim) / 2);
            }

            left = new Button(startingX, valuePosY + hOffset, arrowDim, arrowDim){
                @Override
                protected void onLeftClick() {
                    super.onLeftClick();
                    setting.increment();
                }
            }.setImage(UIThemeManager.getDefaultTheme().arrow_left);
            right = new Button((int)(xPos + width * (1-arrowPercX)), valuePosY + hOffset, arrowDim, arrowDim){
                @Override
                protected void onLeftClick() {
                    super.onLeftClick();
                    setting.decrement();
                }
            }.setImage(UIThemeManager.getDefaultTheme().arrow_right);

            middle = new Inputfield(setting.getCurrentValue().toString(), ((int)(xPos + width * ((1 - valuePercX) + arrowPercX))), valuePosY, ((int)(width * (valuePercX -2* arrowPercX))), valueHeight).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            ((Inputfield)middle).getTextBox().addOnTextChangedConsumer(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    if(s.isEmpty()){
                        ((Inputfield)middle).getTextBox().setText("0");
                        return;
                    }
                    setting.trySetValue(Integer.valueOf(s));
                }
            });
        }
        else{
            middle = new Inputfield(setting.getCurrentValue().toString(), ((int)(xPos + width * (1 - valuePercX))), valuePosY, ((int)(width * valuePercX)), valueHeight).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            ((Inputfield)middle).getTextBox().addOnTextChangedConsumer(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    if(s.isEmpty()) {
                        setting.trySetValue(0);
                        return;
                    }

                    setting.trySetValue(Integer.valueOf(s));
                }
            });
        }

        setting.addOnValueChangedConsumer(new Runnable() {
            @Override
            public void run() {
                Inputfield inputfield = (Inputfield) middle;
                if(!inputfield.getTextBox().getText().equals(setting.getCurrentValue().toString())){
                    inputfield.getTextBox().setText(setting.getCurrentValue().toString());
                }
            }
        });
    }
}
