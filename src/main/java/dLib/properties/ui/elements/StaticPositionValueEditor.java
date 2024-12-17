package dLib.properties.ui.elements;

import dLib.properties.objects.PositionProperty;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.StaticPosition;

public class StaticPositionValueEditor extends PositionValueEditor<StaticPosition> {
    private Inputfield inputfield;

    public StaticPositionValueEditor(StaticPosition value){
        this(new PositionProperty(value));
    }

    public StaticPositionValueEditor(PositionProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.auto());
        {
            inputfield = new Inputfield(property.getValueForDisplay(), Dim.fill(), Dim.px(50));
            inputfield.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
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
