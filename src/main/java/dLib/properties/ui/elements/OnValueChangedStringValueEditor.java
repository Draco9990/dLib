package dLib.properties.ui.elements;

import dLib.properties.objects.StringProperty;

public class OnValueChangedStringValueEditor extends StringValueEditor {
    //region Variables
    //endregion

    //region Constructors

    public OnValueChangedStringValueEditor(String value){
        this(new StringProperty(value));
    }

    public OnValueChangedStringValueEditor(StringProperty property) {
        super(property);

        inputfield.onValueChangedEvent.subscribeManaged(s -> boundProperty.setValueFromString(s));
    }


    //endregion
}
