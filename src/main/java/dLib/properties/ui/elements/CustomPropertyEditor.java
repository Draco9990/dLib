package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.themes.UIThemeManager;
import dLib.util.screens.AbstractObjectListPickerScreenOld;
import dLib.properties.objects.templates.TCustomProperty;

public class CustomPropertyEditor<PropertyType extends TCustomProperty<ItemType, ?>, ItemType> extends AbstractPropertyEditor<PropertyType> {
    //region Variables

    TextButton middleButton;

    //endregion

    //region Constructors

    public CustomPropertyEditor(PropertyType setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(PropertyType property, Integer width, Integer height) {
        middleButton = new TextButton("Edit", 0, 0, width, height);
        middleButton.getButton().addOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                AbstractObjectListPickerScreenOld<ItemType> pickerScreen = new AbstractObjectListPickerScreenOld<ItemType>(property.getAllOptions()) {
                    @Override
                    public void onItemSelected(ItemType item) {
                        super.onItemSelected(item);
                        property.setValue(item);
                    }
                };
                pickerScreen.open();
            }
        });
        middleButton.getButton().setImage(UIThemeManager.getDefaultTheme().button_large_square);

        property.addOnValueChangedListener((itemType, itemType2) -> middleButton.getTextBox().setText(property.getValueForDisplay()));

        return middleButton;
    }

    //endregion
}
