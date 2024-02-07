package dLib.ui.elements.settings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.DLib;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.ScreenManager;
import dLib.ui.screens.util.AbstractObjectListPickerScreen;
import dLib.util.settings.prefabs.CustomSetting;

public class ButtonUISetting extends AbstractUISetting {
    /** Constructor */
    public ButtonUISetting(CustomSetting<Object> setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);

        this.middle = new TextButton(setting.getValueForDisplay(), xPos + ((int)(width * 0.75f)), yPos, (int)(width * 0.25f), height);
        ((TextButton)middle).getButton().setOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                AbstractObjectListPickerScreen<Object> pickerScreen = new AbstractObjectListPickerScreen<Object>(ScreenManager.getCurrentScreen(), setting.getAllOptions()) {
                    @Override
                    public String getModId() {
                        return DLib.getModID();
                    }
                };
                ScreenManager.openScreen(pickerScreen);
            }
        });
    }
}
