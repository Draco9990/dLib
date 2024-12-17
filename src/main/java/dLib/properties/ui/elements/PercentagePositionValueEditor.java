package dLib.properties.ui.elements;

import dLib.properties.objects.PositionProperty;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.PercentagePosition;
import dLib.util.ui.position.StaticPosition;

public class PercentagePositionValueEditor extends PositionValueEditor<PercentagePosition> {
    private Inputfield inputfield;

    public PercentagePositionValueEditor(PositionProperty property, AbstractDimension width, AbstractDimension height) {
        super(property, width, height);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.fill());
        {
            inputfield = new Inputfield(property.getValueForDisplay(), Dim.fill(), Dim.fill());
            inputfield.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_DECIMAL);
            inputfield.addOnValueChangedListener(s -> boundProperty.setValueFromString(s));
            contentBox.addItem(inputfield);

            contentBox.addItem(makeSwapComboBox());
        }

        property.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputfield.getTextBox().getText().equals(boundProperty.getValueForDisplay())){
                inputfield.getTextBox().setText(boundProperty.getValueForDisplay());
            }
        });
    }
}
