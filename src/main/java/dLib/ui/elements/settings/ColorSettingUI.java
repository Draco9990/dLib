package dLib.ui.elements.settings;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.screens.ScreenManager;
import dLib.ui.themes.UITheme;
import dLib.util.screens.ColorPickerScreen;
import dLib.util.settings.prefabs.ColorSetting;

public class ColorSettingUI extends AbstractSettingUI {
    /** Constructor */
    public ColorSettingUI(ColorSetting setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);

        this.middle = new Button( xPos + ((int)(width - width * valuePercX)), valuePosY, (int)(width * valuePercX), valueHeight){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                ScreenManager.openScreen(new ColorPickerScreen(ScreenManager.getCurrentScreen(), setting.getCurrentValue()){
                    @Override
                    public void onColorChosen(Color color) {
                        super.onColorChosen(color);
                        setting.trySetValue(color);
                    }
                });
            }
        }.setImage(UITheme.whitePixel).setRenderColor(setting.getCurrentValue());

        setting.addOnValueChangedConsumer(new Runnable() {
            @Override
            public void run() {
                ((Button)middle).setRenderColor(setting.getCurrentValue());
            }
        });
    }
}
