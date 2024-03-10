package dLib.ui.elements.settings;

import dLib.ui.Alignment;
import dLib.ui.elements.prefabs.TextButton;
import dLib.util.EnumHelpers;
import dLib.util.settings.prefabs.AlignmentProperty;

import java.util.function.BiConsumer;

public class AlignmentSettingUI extends AbstractSettingUI{
    //region Variables

    TextButton leftButton;
    TextButton rightButton;

    //endregion

    //region Constructors

    public AlignmentSettingUI(AlignmentProperty setting, Integer xPos, Integer yPos, Integer width, Integer height) {
        super(setting, xPos, yPos, width, height);

        int renderWidth = (int) (width * valuePercX);
        int startingX = (width - renderWidth);

        int buttonWidth = (int)(0.45f * renderWidth);

        leftButton = new TextButton(setting.getValue().horizontalAlignment.name(), startingX, valuePosY, buttonWidth, valueHeight);
        leftButton.getButton().addOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                Alignment alignment = setting.getValue();
                alignment.horizontalAlignment = (Alignment.HorizontalAlignment) EnumHelpers.nextEnum(alignment.horizontalAlignment);
                setting.setValue(alignment);
            }
        });
        addChildNCS(leftButton);

        rightButton = new TextButton(setting.getValue().verticalAlignment.name(), (int) (startingX + (renderWidth * 0.55f)), valuePosY, buttonWidth, valueHeight);
        rightButton.getButton().addOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                Alignment alignment = setting.getValue();
                alignment.verticalAlignment = (Alignment.VerticalAlignment) EnumHelpers.nextEnum(alignment.verticalAlignment);
                setting.setValue(alignment);
            }
        });
        addChildNCS(rightButton);

        setting.addOnHorizontalAlignmentChangedListener(new BiConsumer<Alignment.HorizontalAlignment, Alignment.HorizontalAlignment>() {
            @Override
            public void accept(Alignment.HorizontalAlignment horizontalAlignment, Alignment.HorizontalAlignment horizontalAlignment2) {
                if(!leftButton.getTextBox().getText().equals(setting.getValue().horizontalAlignment.name())){
                    leftButton.getTextBox().setText(setting.getValue().horizontalAlignment.name());
                }
            }
        });
        setting.addOnVerticalAlignmentChangedListener(new BiConsumer<Alignment.VerticalAlignment, Alignment.VerticalAlignment>() {
            @Override
            public void accept(Alignment.VerticalAlignment verticalAlignment, Alignment.VerticalAlignment verticalAlignment2) {
                if(!rightButton.getTextBox().getText().equals(setting.getValue().verticalAlignment.name())){
                    rightButton.getTextBox().setText(setting.getValue().verticalAlignment.name());
                }
            }
        });
    }

    //endregion

    //region Methods

    @Override
    public boolean onLeftInteraction() {
        leftButton.getButton().trigger();
        return true;
    }

    @Override
    public boolean onRightInteraction() {
        rightButton.getButton().trigger();
        return true;
    }

    //endregion
}
