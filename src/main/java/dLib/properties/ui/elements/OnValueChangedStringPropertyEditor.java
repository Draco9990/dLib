package dLib.properties.ui.elements;

import dLib.properties.objects.StringProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;

public class OnValueChangedStringPropertyEditor extends StringPropertyEditor{
    //region Variables
    //endregion

    //region Constructors

    public OnValueChangedStringPropertyEditor(StringProperty setting, Integer xPos, Integer yPos, Integer width, Integer height) {
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(StringProperty property, Integer width, Integer height) {
        Inputfield element = (Inputfield) super.buildContent(property, width, height);

        element.addOnValueChangedListener(s -> {
            property.setValue(input.getTextBox().getText());
            if(!input.getTextBox().getText().equals(property.getValue())){
                input.getTextBox().setText(property.getValue());
            }
        });

        return element;
    }

    //endregion
}
