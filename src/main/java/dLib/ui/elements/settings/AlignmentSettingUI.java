package dLib.ui.elements.settings;

import dLib.ui.Alignment;
import dLib.ui.elements.prefabs.TextButton;
import dLib.util.EnumHelpers;
import dLib.util.settings.prefabs.AlignmentSetting;

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
                setting.trySetValue(alignment);
            }
        });

        this.right = new TextButton(setting.getCurrentValue().verticalAlignment.name(), (int) (startingX + (renderWidth * 0.55f)), valuePosY, buttonWidth, valueHeight);
        ((TextButton)right).getButton().setOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                Alignment alignment = setting.getCurrentValue();
                alignment.verticalAlignment = (Alignment.VerticalAlignment) EnumHelpers.nextEnum(alignment.verticalAlignment);
                setting.trySetValue(alignment);
            }
        });

        setting.setOnValueChangedConsumer(new Runnable() {
            @Override
            public void run() {
                TextButton leftI = ((TextButton)left);
                if(!leftI.getTextBox().getText().equals(setting.getCurrentValue().horizontalAlignment.name())){
                    leftI.getTextBox().setText(setting.getCurrentValue().horizontalAlignment.name());
                }

                TextButton rightI = ((TextButton)right);
                if(!rightI.getTextBox().getText().equals(setting.getCurrentValue().verticalAlignment.name())){
                    rightI.getTextBox().setText(setting.getCurrentValue().verticalAlignment.name());
                }
            }
        });
    }
}
