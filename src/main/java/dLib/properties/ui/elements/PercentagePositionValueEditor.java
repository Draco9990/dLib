package dLib.properties.ui.elements;

import dLib.properties.objects.PositionProperty;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.PercentagePosition;

public class PercentagePositionValueEditor extends PositionValueEditor<PercentagePosition> {
    private Inputfield inputfield;

    public PercentagePositionValueEditor(PercentagePosition value){
        this(new PositionProperty(value));
    }

    public PercentagePositionValueEditor(PositionProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.auto());
        {
            inputfield = new Inputfield(String.valueOf(((PercentagePosition)property.getValue()).getValueRaw()), Dim.fill(), Dim.px(50));
            inputfield.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_DECIMAL_POSITIVE);
            inputfield.onValueChangedEvent.subscribeManaged(s -> boundProperty.setValueFromString(s));
            contentBox.addChild(inputfield);

            contentBox.addChild(makeSwapComboBox());
        }
        addChild(contentBox);

        property.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputfield.textBox.getText().equals(String.valueOf(((PercentagePosition)boundProperty.getValue()).getValueRaw()))){
                inputfield.textBox.setText(String.valueOf(((PercentagePosition)boundProperty.getValue()).getValueRaw()));
            }
        });
    }
}
