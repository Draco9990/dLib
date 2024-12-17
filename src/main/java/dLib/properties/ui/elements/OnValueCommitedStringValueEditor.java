package dLib.properties.ui.elements;

import dLib.util.ui.dimensions.AbstractDimension;

public class OnValueCommitedStringValueEditor extends StringValueEditor {
    //region Variables
    //endregion

    //region Constructors

    public OnValueCommitedStringValueEditor(String value, AbstractDimension width, AbstractDimension height) {
        super(value, width, height);

        inputfield.addOnValueCommittedListener(s -> delayedActions.add(() -> {
            setValueEvent.invoke(objectConsumer -> objectConsumer.accept(s));
        }));
    }


    //endregion
}
