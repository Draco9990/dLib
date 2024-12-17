package dLib.properties.ui.elements;

import dLib.properties.objects.StringProperty;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.ui.dimensions.AbstractDimension;

public abstract class StringValueEditor extends AbstractValueEditor<String, StringProperty> {
    //region Variables

    protected Inputfield inputfield;

    //endregion

    //region Constructors

    public StringValueEditor(String value, AbstractDimension width, AbstractDimension height){
        this(new StringProperty(value), width, height);
    }

    public StringValueEditor(StringProperty property, AbstractDimension width, AbstractDimension height) {
        super(property, width, height);

        inputfield = new Inputfield(property.getValueForDisplay(), width, height);

        property.onValueChangedEvent.subscribe(this, (oldVal, newValue) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputfield.getTextBox().getText().equals(newValue)){
                inputfield.getTextBox().setText(newValue);
            }
        });

        addChildNCS(inputfield);
    }


    //endregion
}
