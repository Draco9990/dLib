package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.themes.UIThemeManager;
import dLib.util.settings.prefabs.EnumProperty;

import java.util.function.BiConsumer;

public class EnumSettingUI extends AbstractSettingUI {
    //region Variables

    Button leftArrow;
    Button rightArrow;

    TextButton middleButton;

    //endregion

    //region Constructors

    public EnumSettingUI(EnumProperty<? extends Enum<?>> setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);

        int startingX = (int) (xPos + width * (1 - valuePercX));

        int arrowDim = Math.min((int)(arrowPercX * width), valueHeight);

        int hOffset = 0;
        if(arrowDim != height){
            hOffset = (int)((height-arrowDim) / 2);
        }

        leftArrow = new Button(startingX, valuePosY + hOffset, arrowDim, arrowDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                setting.previous();
            }
        }.setImage(UIThemeManager.getDefaultTheme().arrow_left);
        addChildNCS(leftArrow);

        rightArrow = new Button((int)(xPos + width * (1- arrowPercX)), valuePosY + hOffset, arrowDim, arrowDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                setting.next();
            }
        }.setImage(UIThemeManager.getDefaultTheme().arrow_right);
        addChildNCS(rightArrow);

        middleButton = new TextButton(setting.getValueForDisplay(), ((int)(xPos + width * ((1-valuePercX) + arrowPercX))), valuePosY, ((int)(width * (valuePercX - arrowPercX *2))), valueHeight);
        addChildCS(middleButton);

        setting.addOnValueChangedListener(new Runnable() {
            @Override
            public void run() {
                if(!middleButton.getTextBox().getText().equals(setting.getValueForDisplay())){
                    middleButton.getTextBox().setText(setting.getValueForDisplay());
                }
            }
        });
    }

    //endregion

    //region Methods

    @Override
    public boolean onLeftInteraction() {
        leftArrow.trigger();
        return true;
    }

    @Override
    public boolean onRightInteraction() {
        rightArrow.trigger();
        return true;
    }

    //endregion
}
