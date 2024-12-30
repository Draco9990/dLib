package dLib.properties.ui.elements;

import dLib.properties.objects.StringProperty;
import dLib.ui.elements.items.input.Inputfield;
import dLib.util.ui.dimensions.Dim;

public abstract class StringValueEditor extends AbstractValueEditor<String, StringProperty> {
    //region Variables

    protected Inputfield inputfield;

    //endregion

    //region Constructors

    public StringValueEditor(StringProperty property) {
        super(property);

        inputfield = new Inputfield(property.getValueForDisplay(), Dim.fill(), Dim.px(50));

        property.onValueChangedEvent.subscribe(this, (oldVal, newValue) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputfield.textBox.getText().equals(newValue)){
                inputfield.textBox.setText(newValue);
            }
        });

        addChildNCS(inputfield);
    }


    //endregion
}
