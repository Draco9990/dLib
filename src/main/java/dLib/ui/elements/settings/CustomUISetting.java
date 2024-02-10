package dLib.ui.elements.settings;

import dLib.DLib;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.ScreenManager;
import dLib.ui.screens.util.AbstractObjectListPickerScreen;
import dLib.util.settings.prefabs.CustomSetting;

public class CustomUISetting<ItemType> extends AbstractUISetting {
    /** Constructor */
    public CustomUISetting(CustomSetting<ItemType> setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);

        this.middle = new TextButton(setting.getValueForDisplay(), xPos + ((int)(width * textPerc)), yPos, (int)(width * (1-textPerc)), height);
        ((TextButton)middle).getButton().setOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                AbstractObjectListPickerScreen<ItemType> pickerScreen = new AbstractObjectListPickerScreen<ItemType>(ScreenManager.getCurrentScreen(), setting.getAllOptions()) {
                    @Override
                    public void onItemSelected(ItemType item) {
                        super.onItemSelected(item);
                        setting.setCurrentValue(item);
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
                ((TextButton)middle).getLabel().setText(setting.getValueForDisplay());
            }
        });
    }
}
