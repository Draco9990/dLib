package dLib.properties.ui.elements;

import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.ui.dimensions.AbstractDimension;

public class ArrayValueEditor<ItemType> extends AbstractValueEditor<ItemType> {
    //region Variables

    TextButton middleButton;

    //endregion

    //region Constructors

    public ArrayValueEditor(ItemType array, AbstractDimension width, AbstractDimension height) {
        super(width, height);

        //TODO - replace with a list of editors for the given object, with the ability to add or remove itmes

        middleButton = new TextButton("Edit", width, height);
        //TODO

        middleButton.getButton().setImage(UICommonResources.button02_horizontal);
        addChildNCS(middleButton);

        onValueChangedEvent.subscribe(ArrayValueEditor.this, (newValue) -> middleButton.getTextBox().setText("")); //TODO
    }


    //endregion
}
