package dLib.ui.elements.settings;

import dLib.ui.Alignment;
import dLib.ui.elements.prefabs.TextButton;
import dLib.util.EnumHelpers;
import dLib.util.settings.prefabs.AlignmentProperty;

public class AlignmentSettingUI extends AbstractSettingUI{
    /** Variables */

    /** Constructors */
    public AlignmentSettingUI(AlignmentProperty setting, Integer xPos, Integer yPos, Integer width, Integer height) {
        super(setting, xPos, yPos, width, height);

        int renderWidth = (int) (width * valuePercX);
        int startingX = xPos + (width - renderWidth);

        int buttonWidth = (int)(0.45f * renderWidth);

        this.left = new TextButton(setting.getValue().horizontalAlignment.name(), startingX, valuePosY, buttonWidth, valueHeight);
        ((TextButton)left).getButton().addOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                Alignment alignment = setting.getValue();
                alignment.horizontalAlignment = (Alignment.HorizontalAlignment) EnumHelpers.nextEnum(alignment.horizontalAlignment);
                setting.setValue(alignment);
            }
        });

        this.right = new TextButton(setting.getValue().verticalAlignment.name(), (int) (startingX + (renderWidth * 0.55f)), valuePosY, buttonWidth, valueHeight);
        ((TextButton)right).getButton().addOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                Alignment alignment = setting.getValue();
                alignment.verticalAlignment = (Alignment.VerticalAlignment) EnumHelpers.nextEnum(alignment.verticalAlignment);
                setting.setValue(alignment);
            }
        });

        setting.addOnValueChangedListener(new Runnable() {
            @Override
            public void run() {
                TextButton leftI = ((TextButton)left);
                if(!leftI.getTextBox().getText().equals(setting.getValue().horizontalAlignment.name())){
                    leftI.getTextBox().setText(setting.getValue().horizontalAlignment.name());
                }

                TextButton rightI = ((TextButton)right);
                if(!rightI.getTextBox().getText().equals(setting.getValue().verticalAlignment.name())){
                    rightI.getTextBox().setText(setting.getValue().verticalAlignment.name());
                }
            }
        });
    }
}
