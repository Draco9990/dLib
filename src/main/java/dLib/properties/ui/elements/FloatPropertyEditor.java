package dLib.properties.ui.elements;

import dLib.properties.objects.FloatProperty;
import dLib.properties.objects.IntegerProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;

public class FloatPropertyEditor extends AbstractPropertyEditor<FloatProperty> {
    //region Variables

    Inputfield middleInputfield;

    //endregion

    //region Constructors

    public FloatPropertyEditor(FloatProperty setting, Integer xPos, Integer yPos, Integer width, int height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(FloatProperty property, Integer width, Integer height) {
        middleInputfield = new Inputfield(property.getValue().toString(), 0, 0, width, height).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_DECIMAL);
        middleInputfield.getTextBox().addOnTextChangedConsumer(s -> {
            if(s.isEmpty()) {
                property.setValue(0f);
                return;
            }

            property.setValue(Float.valueOf(s));
        });

        property.addOnValueChangedListener((integer, integer2) -> {
            if(!middleInputfield.getTextBox().getText().equals(property.getValue().toString())){
                middleInputfield.getTextBox().setText(property.getValue().toString());
            }
        });

        return middleInputfield;
    }

    //endregion

    /** Constructors */
}
