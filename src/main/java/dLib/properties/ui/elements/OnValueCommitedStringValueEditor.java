package dLib.properties.ui.elements;

import dLib.properties.objects.StringProperty;

public class OnValueCommitedStringValueEditor extends StringValueEditor {
    //region Variables
    //endregion

    //region Constructors

    public OnValueCommitedStringValueEditor(String value){
        this(new StringProperty(value));
    }

    public OnValueCommitedStringValueEditor(StringProperty property) {
        super(property);

        inputfield.addOnValueCommittedListener(s -> boundProperty.setValueFromString(s));
    }


    //endregion
}
