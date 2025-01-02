package dLib.properties.ui.elements;

import dLib.properties.objects.DimensionProperty;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.input.Inputfield;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.PixelDimension;

public class PixelDimensionValueEditor extends DimensionValueEditor<PixelDimension> {
    private Inputfield inputfield;

    public PixelDimensionValueEditor(PixelDimension value){
        this(new DimensionProperty(value));
    }

    public PixelDimensionValueEditor(DimensionProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.auto());
        {
            inputfield = new Inputfield(String.valueOf(((PixelDimension)property.getValue()).getValueRaw()), Dim.fill(), Dim.px(50));
            inputfield.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            inputfield.onValueChangedEvent.subscribeManaged(s -> boundProperty.setValueFromString(s));
            contentBox.addItem(inputfield);

            contentBox.addItem(makeSwapComboBox());
        }
        addChild(contentBox);

        property.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputfield.textBox.getText().equals(String.valueOf(((PixelDimension)boundProperty.getValue()).getValueRaw()))){
                inputfield.textBox.setText(String.valueOf(((PixelDimension)boundProperty.getValue()).getValueRaw()));
            }
        });
    }
}
