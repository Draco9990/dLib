package dLib.ui.elements.settings;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.IntVector2;
import dLib.util.settings.prefabs.IntVector2Setting;

import java.util.function.Consumer;

public class IntVector2SettingUI extends AbstractSettingUI{
    /** Variables */

    /** Constructors */
    public IntVector2SettingUI(IntVector2Setting setting, Integer xPos, Integer yPos, Integer width, Integer height) {
        super(setting, xPos, yPos, width, height);

        int renderWidth = (int) (width * valuePercX);
        int startingX = xPos + (width - renderWidth);

        int textWidth = (int) (0.2f * renderWidth);
        int inputfieldWidth = (int)(0.25f * renderWidth);

        this.foreground.add(new TextBox(setting.getXAxisName(), startingX, valuePosY, textWidth, valueHeight, 0.15f, 0.15f).setTextRenderColor(Color.WHITE));
        this.foreground.add(new TextBox(setting.getYAxisName(), startingX + ((int)(renderWidth * 0.55f)), valuePosY, textWidth, valueHeight, 0.15f, 0.15f).setTextRenderColor(Color.WHITE));

        this.left = new Inputfield(String.valueOf(setting.getCurrentValue().x), startingX + textWidth, valuePosY, inputfieldWidth, valueHeight).setType(Inputfield.EInputfieldType.NUMERICAL_WHOLE_POSITIVE);
        ((Inputfield)left).getTextBox().setOnTextChangedConsumer(new Consumer<String>() {
            @Override
            public void accept(String s) {
                IntVector2 currentVal = setting.getCurrentValue();
                if(s.isEmpty()) {
                    currentVal.x = 0;
                }
                else{
                    currentVal.x = Integer.valueOf(s);
                }

                setting.setCurrentValue(currentVal);
            }
        });

        this.right = new Inputfield(String.valueOf(setting.getCurrentValue().y), startingX + (int)(renderWidth * 0.55f) + textWidth, valuePosY, inputfieldWidth, valueHeight).setType(Inputfield.EInputfieldType.NUMERICAL_WHOLE_POSITIVE);
        ((Inputfield)right).getTextBox().setOnTextChangedConsumer(new Consumer<String>() {
            @Override
            public void accept(String s) {
                IntVector2 currentVal = setting.getCurrentValue();
                if(s.isEmpty()) {
                    currentVal.y = 0;
                }
                else{
                    currentVal.y = Integer.parseInt(s);
                }

                setting.setCurrentValue(currentVal);
            }
        });

        setting.setOnValueChangedConsumer(new Runnable() {
            @Override
            public void run() {
                Inputfield leftI = ((Inputfield)left);
                if(!leftI.getTextBox().getText().equals(String.valueOf(setting.getCurrentValue().x))){
                    leftI.getTextBox().setText(String.valueOf(setting.getCurrentValue().x));
                }

                Inputfield rightI = ((Inputfield)right);
                if(!rightI.getTextBox().getText().equals(String.valueOf(setting.getCurrentValue().y))){
                    rightI.getTextBox().setText(String.valueOf(setting.getCurrentValue().y));
                }
            }
        });
    }
}
