package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.properties.objects.StringProperty;

public class StringPropertyEditor extends AbstractPropertyEditor<StringProperty> {
    //region Variables

    Inputfield input;

    //endregion

    //region Constructors

    public StringPropertyEditor(StringProperty setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(StringProperty property, Integer width, Integer height) {
        input = new Inputfield(property.getValue(), 0, 0, width, height);
        input.addOnValueCommittedListener(s -> {
            property.setValue(input.getTextBox().getText());
            if(!input.getTextBox().getText().equals(property.getValue())){
                input.getTextBox().setText(property.getValue());
            }
        });

        property.addOnValueChangedListener((s, s2) -> {
            if(!input.getTextBox().getText().equals(property.getValue())){
                input.getTextBox().setText(property.getValue());
            }
        });

        return input;
    }


    //endregion
}
