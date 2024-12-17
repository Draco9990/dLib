package dLib.properties.ui.elements;

import dLib.properties.objects.DimensionProperty;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.PercentageDimension;

public class PercentageDimensionValueEditor extends DimensionValueEditor<PercentageDimension> {
    private Inputfield inputfield;

    public PercentageDimensionValueEditor(PercentageDimension value){
        this(new DimensionProperty(value));
    }

    public PercentageDimensionValueEditor(DimensionProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.auto());
        {
            inputfield = new Inputfield(property.getValueForDisplay(), Dim.fill(), Dim.px(50));
            inputfield.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            inputfield.addOnValueChangedListener(s -> boundProperty.setValueFromString(s));
            contentBox.addItem(inputfield);

            contentBox.addItem(makeSwapComboBox());
        }
        addChildNCS(contentBox);

        property.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputfield.getTextBox().getText().equals(boundProperty.getValueForDisplay())){
                inputfield.getTextBox().setText(boundProperty.getValueForDisplay());
            }
        });
    }
}
