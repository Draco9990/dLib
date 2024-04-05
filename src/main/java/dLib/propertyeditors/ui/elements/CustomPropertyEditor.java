package dLib.propertyeditors.ui.elements;

import dLib.DLib;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.ScreenManager;
import dLib.util.screens.AbstractObjectListPickerScreenOld;
import dLib.util.settings.prefabs.CustomProperty;

public class CustomPropertyEditor<ItemType> extends AbstractPropertyEditor<CustomProperty<ItemType>> {
    //region Variables

    TextButton middleButton;

    //endregion

    //region Constructors

    public CustomPropertyEditor(CustomProperty<ItemType> setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(CustomProperty<ItemType> property, Integer width, Integer height) {
        middleButton = new TextButton(property.getValueForDisplay(), 0, 0, width, height);
        middleButton.getButton().addOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                AbstractObjectListPickerScreenOld<ItemType> pickerScreen = new AbstractObjectListPickerScreenOld<ItemType>(ScreenManager.getCurrentScreen(), property.getAllOptions()) {
                    @Override
                    public void onItemSelected(ItemType item) {
                        super.onItemSelected(item);
                        property.setValue(item);
                    }

                    @Override
                    public String getModId() {
                        return DLib.getModID();
                    }
                };
                ScreenManager.openScreen(pickerScreen);
            }
        });

        property.addOnValueChangedListener((itemType, itemType2) -> middleButton.getTextBox().setText(property.getValueForDisplay()));

        return middleButton;
    }

    //endregion
}
