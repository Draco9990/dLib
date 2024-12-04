package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.properties.objects.templates.TStringProperty;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

public abstract class StringPropertyEditor extends AbstractPropertyEditor<TStringProperty<?>> {
    //region Variables

    Inputfield input;

    //endregion

    //region Constructors

    public StringPropertyEditor(TStringProperty setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TStringProperty<?> property, AbstractDimension width, AbstractDimension height) {
        input = new Inputfield(property.getValue(), width, height);

        property.addOnValueChangedListener((s, s2) -> {
            if(!input.getTextBox().getText().equals(property.getValue())){
                input.getTextBox().setText(property.getValue());
            }
        });

        return input;
    }


    //endregion
}
