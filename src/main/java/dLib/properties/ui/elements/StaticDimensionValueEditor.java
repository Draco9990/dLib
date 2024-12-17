package dLib.properties.ui.elements;

import dLib.properties.objects.DimensionProperty;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.StaticDimension;
import dLib.util.ui.position.StaticPosition;

public class StaticDimensionValueEditor extends DimensionValueEditor<StaticDimension> {
    private Inputfield inputfield;

    public StaticDimensionValueEditor(StaticDimension value){
        this(new DimensionProperty(value));
    }

    public StaticDimensionValueEditor(DimensionProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.auto());
        {
            inputfield = new Inputfield(String.valueOf(((StaticDimension)property.getValue()).getValueRaw()), Dim.fill(), Dim.px(50));
            inputfield.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            inputfield.addOnValueChangedListener(s -> boundProperty.setValueFromString(s));
            contentBox.addItem(inputfield);

            contentBox.addItem(makeSwapComboBox());
        }
        addChildNCS(contentBox);

        property.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputfield.getTextBox().getText().equals(String.valueOf(((StaticDimension)boundProperty.getValue()).getValueRaw()))){
                inputfield.getTextBox().setText(String.valueOf(((StaticDimension)boundProperty.getValue()).getValueRaw()));
            }
        });
    }
}
