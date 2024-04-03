package dLib.ui.elements.settings;

import dLib.DLib;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.ScreenManager;
import dLib.util.screens.AbstractObjectListPickerScreenOld;
import dLib.util.settings.prefabs.CustomProperty;

import java.util.function.BiConsumer;

public class CustomSettingUI<ItemType> extends AbstractSettingUI {
    //region Variables

    TextButton middleButton;

    //endregion

    //region Constructors

    public CustomSettingUI(CustomProperty<ItemType> setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);

        middleButton = new TextButton(setting.getValueForDisplay(), ((int)(width - width * valuePercX)), valuePosY, (int)(width * valuePercX), valueHeight);
        middleButton.getButton().addOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                AbstractObjectListPickerScreenOld<ItemType> pickerScreen = new AbstractObjectListPickerScreenOld<ItemType>(ScreenManager.getCurrentScreen(), setting.getAllOptions()) {
                    @Override
                    public void onItemSelected(ItemType item) {
                        super.onItemSelected(item);
                        setting.setValue(item);
                    }

                    @Override
                    public String getModId() {
                        return DLib.getModID();
                    }
                };
                ScreenManager.openScreen(pickerScreen);
            }
        });
        addChildCS(middleButton);

        setting.addOnValueChangedListener(new BiConsumer<ItemType, ItemType>() {
            @Override
            public void accept(ItemType itemType, ItemType itemType2) {
                middleButton.getTextBox().setText(setting.getValueForDisplay());
            }
        });
    }

    //endregion

    //region Methods
    //endregion
}
