package dLib.ui.elements.settings;

import dLib.DLib;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.ScreenManager;
import dLib.ui.screens.util.AbstractObjectListPickerScreen;
import dLib.util.settings.prefabs.CustomSetting;

public class CustomUISetting extends AbstractUISetting {
    /** Constructor */
    public CustomUISetting(CustomSetting<Object> setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);

        this.middle = new TextButton(setting.getValueForDisplay(), xPos + ((int)(width * textPerc)), yPos, (int)(width * (1-textPerc)), height);
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
