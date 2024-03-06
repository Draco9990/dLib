package dLib.ui.elements.settings;

import dLib.DLib;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.ScreenManager;
import dLib.util.screens.AbstractObjectListPickerScreen;
import dLib.util.settings.prefabs.CustomSetting;

public class CustomSettingUI<ItemType> extends AbstractSettingUI {
    /** Constructor */
    public CustomSettingUI(CustomSetting<ItemType> setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);

        this.middle = new TextButton(setting.getValueForDisplay(), xPos + ((int)(width - width * valuePercX)), valuePosY, (int)(width * valuePercX), valueHeight);
        ((TextButton)middle).getButton().setOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                AbstractObjectListPickerScreen<ItemType> pickerScreen = new AbstractObjectListPickerScreen<ItemType>(ScreenManager.getCurrentScreen(), setting.getAllOptions()) {
                    @Override
                    public void onItemSelected(ItemType item) {
                        super.onItemSelected(item);
                        setting.trySetValue(item);
                    }

                    @Override
                    public String getModId() {
                        return DLib.getModID();
                    }
                };
                ScreenManager.openScreen(pickerScreen);
            }
        });

        setting.setOnValueChangedConsumer(new Runnable() {
            @Override
            public void run() {
                ((TextButton)middle).getTextBox().setText(setting.getValueForDisplay());
            }
        });
    }
}
