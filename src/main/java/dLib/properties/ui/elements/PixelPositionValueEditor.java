package dLib.properties.ui.elements;

import dLib.properties.objects.PositionProperty;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.input.Inputfield;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.PixelPosition;

public class PixelPositionValueEditor extends PositionValueEditor<PixelPosition> {
    private Inputfield inputfield;

    public PixelPositionValueEditor(PixelPosition value){
        this(new PositionProperty(value));
    }

    public PixelPositionValueEditor(PositionProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.auto());
        {
            inputfield = new Inputfield(String.valueOf(((PixelPosition)property.getValue()).getValueRaw()), Dim.fill(), Dim.px(50));
            inputfield.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            inputfield.addOnValueChangedListener(s -> boundProperty.setValueFromString(s));
            contentBox.addItem(inputfield);

            contentBox.addItem(makeSwapComboBox());
        }
        addChildNCS(contentBox);

        property.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputfield.getTextBox().getText().equals(String.valueOf(((PixelPosition)boundProperty.getValue()).getValueRaw()))){
                inputfield.getTextBox().setText(String.valueOf(((PixelPosition)boundProperty.getValue()).getValueRaw()));
            }
        });
    }
}
