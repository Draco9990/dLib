package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.properties.objects.IntegerProperty;

public class IntegerPropertyEditor extends AbstractPropertyEditor<IntegerProperty> {
    //region Variables

    Inputfield middleInputfield;

    //endregion

    //region Constructors

    public IntegerPropertyEditor(IntegerProperty setting, Integer xPos, Integer yPos, Integer width, int height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(IntegerProperty property, Integer width, Integer height) {
        middleInputfield = new Inputfield(property.getValue().toString(), 0, 0, width, height).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
        middleInputfield.getTextBox().addOnTextChangedConsumer(s -> {
            if(s.isEmpty()) {
                property.setValue(0);
                return;
            }

            property.setValue(Integer.valueOf(s));
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
