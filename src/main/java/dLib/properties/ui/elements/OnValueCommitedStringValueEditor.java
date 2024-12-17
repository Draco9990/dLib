package dLib.properties.ui.elements;

import dLib.properties.objects.StringProperty;
import dLib.util.ui.dimensions.AbstractDimension;

public class OnValueCommitedStringValueEditor extends StringValueEditor {
    //region Variables
    //endregion

    //region Constructors

    public OnValueCommitedStringValueEditor(String value, AbstractDimension width, AbstractDimension height){
        this(new StringProperty(value), width, height);
    }

    public OnValueCommitedStringValueEditor(StringProperty property, AbstractDimension width, AbstractDimension height) {
        super(property, width, height);

        inputfield.addOnValueCommittedListener(s -> boundProperty.setValueFromString(s));
    }


    //endregion
}
