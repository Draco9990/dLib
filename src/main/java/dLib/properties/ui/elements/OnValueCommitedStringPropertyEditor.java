package dLib.properties.ui.elements;

import dLib.properties.objects.templates.TStringProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

public class OnValueCommitedStringPropertyEditor extends StringPropertyEditor{
    //region Variables
    //endregion

    //region Constructors

    public OnValueCommitedStringPropertyEditor(TStringProperty setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline) {
        super(setting, xPos, yPos, width, multiline);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TStringProperty<?> property, AbstractDimension width, AbstractDimension height) {
        Inputfield element = (Inputfield) super.buildContent(property, width, height);

        element.addOnValueCommittedListener(s -> {
            property.setValue(input.getTextBox().getText());
            if(!input.getTextBox().getText().equals(property.getValue())){
                input.getTextBox().setText(property.getValue());
            }
        });

        if(property.getCharacterLimit() != -1){
            element.setCharacterLimit(property.getCharacterLimit());
        }

        return element;
    }

    //endregion
}
