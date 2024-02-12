package dLib.ui.elements.settings;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.util.EnumHelpers;
import dLib.util.IntVector2;
import dLib.util.settings.prefabs.AlignmentSetting;
import dLib.util.settings.prefabs.IntVector2Setting;

import java.util.function.Consumer;

public class AlignmentSettingUI extends AbstractSettingUI{
    /** Variables */

    /** Constructors */
    public AlignmentSettingUI(AlignmentSetting setting, Integer xPos, Integer yPos, Integer width, Integer height) {
        super(setting, xPos, yPos, width, height);

        int renderWidth = (int) (width * valuePercX);
        int startingX = xPos + (width - renderWidth);

        int buttonWidth = (int)(0.45f * renderWidth);

        this.left = new TextButton(setting.getCurrentValue().horizontalAlignment.name(), startingX, valuePosY, buttonWidth, valueHeight);
        ((TextButton)left).getButton().setOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                Alignment alignment = setting.getCurrentValue();
                alignment.horizontalAlignment = (Alignment.HorizontalAlignment) EnumHelpers.nextEnum(alignment.horizontalAlignment);
                setting.setCurrentValue(alignment);
            }
        });

        this.right = new TextButton(setting.getCurrentValue().verticalAlignment.name(), (int) (startingX + (renderWidth * 0.55f)), valuePosY, buttonWidth, valueHeight);
        ((TextButton)right).getButton().setOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                Alignment alignment = setting.getCurrentValue();
                alignment.verticalAlignment = (Alignment.VerticalAlignment) EnumHelpers.nextEnum(alignment.verticalAlignment);
                setting.setCurrentValue(alignment);
            }
        });

        setting.setOnValueChangedConsumer(new Runnable() {
            @Override
            public void run() {
                TextButton leftI = ((TextButton)left);
                if(!leftI.getLabel().getText().equals(setting.getCurrentValue().horizontalAlignment.name())){
                    leftI.getLabel().setText(setting.getCurrentValue().horizontalAlignment.name());
                }

                TextButton rightI = ((TextButton)right);
                if(!rightI.getLabel().getText().equals(setting.getCurrentValue().verticalAlignment.name())){
                    rightI.getLabel().setText(setting.getCurrentValue().verticalAlignment.name());
                }
            }
        });
    }
}
