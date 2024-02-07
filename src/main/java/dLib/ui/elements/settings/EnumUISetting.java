package dLib.ui.elements.settings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.DLib;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.elements.prefabs.Toggle;
import dLib.ui.screens.ScreenManager;
import dLib.ui.screens.util.AbstractObjectListPickerScreen;
import dLib.ui.themes.UIThemeManager;
import dLib.util.settings.prefabs.EnumSetting;

public class EnumUISetting extends AbstractUISetting {
    /** Variables */

    /** Constructors*/
    public EnumUISetting(EnumSetting<?> setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);

        if(setting.getControlType() == EnumSetting.EControlType.ARROWS){
            int arrowDim = Math.min((int)(2.5f * width), height);

            int hOffset = 0;
            if(arrowDim != height){
                hOffset = (int)((height-arrowDim) / 2);
            }

            left = new Button((int)(xPos + width * textPerc), yPos + hOffset, arrowDim, arrowDim){
                @Override
                protected void onLeftClick() {
                    super.onLeftClick();
                    setting.previous();
                }
            }.setImage(UIThemeManager.getDefaultTheme().arrow_left);
            right = new Button((int)(xPos + width * (1-arrowPerc)), yPos + hOffset, arrowDim, arrowDim){
                @Override
                protected void onLeftClick() {
                    super.onLeftClick();
                    setting.next();
                }
            }.setImage(UIThemeManager.getDefaultTheme().arrow_right);

            middle = new TextButton(setting.getValueForDisplay(), ((int)(xPos + width * (textPerc+arrowPerc))), yPos, ((int)(width * ((1-textPerc)-arrowPerc*2))), height);
        }
        else if(setting.getControlType() == EnumSetting.EControlType.CLICK){
            middle = new TextButton(setting.getValueForDisplay(), ((int)(xPos + width * (textPerc+arrowPerc))), yPos, ((int)(width * (1-textPerc))), height);
            ((TextButton)middle).getButton().setOnLeftClickConsumer(setting::next);
        }

        setting.setOnValueChangedConsumer(new Runnable() {
            @Override
            public void run() {
                TextButton element = (TextButton) middle;
                if(!element.getLabel().getText().equals(setting.getValueForDisplay())){
                    element.getLabel().setText(setting.getValueForDisplay());
                }
            }
        });
    }
}
