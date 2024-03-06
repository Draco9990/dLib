package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.themes.UIThemeManager;
import dLib.util.settings.prefabs.EnumSetting;

public class EnumSettingUI extends AbstractSettingUI {
    /** Variables */

    /** Constructors*/
    public EnumSettingUI(EnumSetting<?> setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);

        int startingX = (int) (xPos + width * (1 - valuePercX));

        if(setting.getControlType() == EnumSetting.EControlType.ARROWS){
            int arrowDim = Math.min((int)(arrowPercX * width), valueHeight);

            int hOffset = 0;
            if(arrowDim != height){
                hOffset = (int)((height-arrowDim) / 2);
            }

            left = new Button(startingX, valuePosY + hOffset, arrowDim, arrowDim){
                @Override
                protected void onLeftClick() {
                    super.onLeftClick();
                    setting.previous();
                }
            }.setImage(UIThemeManager.getDefaultTheme().arrow_left);
            right = new Button((int)(xPos + width * (1- arrowPercX)), valuePosY + hOffset, arrowDim, arrowDim){
                @Override
                protected void onLeftClick() {
                    super.onLeftClick();
                    setting.next();
                }
            }.setImage(UIThemeManager.getDefaultTheme().arrow_right);

            middle = new TextButton(setting.getValueForDisplay(), ((int)(xPos + width * ((1-valuePercX) + arrowPercX))), valuePosY, ((int)(width * (valuePercX - arrowPercX *2))), valueHeight);
        }
        else if(setting.getControlType() == EnumSetting.EControlType.CLICK){
            middle = new TextButton(setting.getValueForDisplay(), ((int)(xPos + width * ((1-valuePercX)))), valuePosY, ((int)(width * (valuePercX - textPercX))), valueHeight);
            ((TextButton)middle).getButton().setOnLeftClickConsumer(setting::next);
        }

        setting.setOnValueChangedConsumer(new Runnable() {
            @Override
            public void run() {
                TextButton element = (TextButton) middle;
                if(!element.getTextBox().getText().equals(setting.getValueForDisplay())){
                    element.getTextBox().setText(setting.getValueForDisplay());
                }
            }
        });
    }
}
