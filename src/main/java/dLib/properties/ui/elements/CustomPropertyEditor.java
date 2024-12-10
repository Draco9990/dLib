package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.themes.UIThemeManager;
import dLib.util.screens.AbstractObjectListPickerScreenOld;
import dLib.properties.objects.templates.TCustomProperty;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

public class CustomPropertyEditor<PropertyType extends TCustomProperty<ItemType, ?>, ItemType> extends AbstractPropertyEditor<PropertyType> {
    //region Variables

    TextButton middleButton;

    //endregion

    //region Constructors

    public CustomPropertyEditor(PropertyType setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline){
        super(setting, xPos, yPos, width, multiline);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(PropertyType property, AbstractDimension width, AbstractDimension height) {
        middleButton = new TextButton("Edit", width, height);
        middleButton.getButton().addOnLeftClickEvent(new Runnable() {
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
