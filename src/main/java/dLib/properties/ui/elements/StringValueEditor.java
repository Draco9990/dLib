package dLib.properties.ui.elements;

import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.ui.dimensions.AbstractDimension;

public abstract class StringValueEditor extends AbstractValueEditor<String> {
    //region Variables

    protected Inputfield inputfield;

    //endregion

    //region Constructors

    public StringValueEditor(String value, AbstractDimension width, AbstractDimension height) {
        super(width, height);

        inputfield = new Inputfield(value, width, height);

        onValueChangedEvent.subscribe(this, (newValue) -> {
            if(!inputfield.getTextBox().getText().equals(newValue)){
                inputfield.getTextBox().setText(newValue);
            }
        });

        addChildNCS(inputfield);
    }


    //endregion
}
