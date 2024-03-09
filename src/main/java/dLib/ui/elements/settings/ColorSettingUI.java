package dLib.ui.elements.settings;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.screens.ScreenManager;
import dLib.ui.themes.UITheme;
import dLib.util.screens.ColorPickerScreen;
import dLib.util.settings.prefabs.ColorProperty;

public class ColorSettingUI extends AbstractSettingUI {
    /** Constructor */
    public ColorSettingUI(ColorProperty setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);

        this.middle = new Button( xPos + ((int)(width - width * valuePercX)), valuePosY, (int)(width * valuePercX), valueHeight){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                ScreenManager.openScreen(new ColorPickerScreen(ScreenManager.getCurrentScreen(), setting.getValue()){
                    @Override
                    public void onColorChosen(Color color) {
                        super.onColorChosen(color);
                        setting.setValue(color);
                    }
                });
            }
        }.setImage(UITheme.whitePixel).setRenderColor(setting.getValue());

        setting.addOnValueChangedListener(new Runnable() {
            @Override
            public void run() {
                ((Button)middle).setRenderColor(setting.getValue());
            }
        });
    }
}
