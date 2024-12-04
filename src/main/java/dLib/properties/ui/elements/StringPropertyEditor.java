package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.properties.objects.templates.TStringProperty;

public abstract class StringPropertyEditor extends AbstractPropertyEditor<TStringProperty<?>> {
    //region Variables

    Inputfield input;

    //endregion

    //region Constructors

    public StringPropertyEditor(TStringProperty setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TStringProperty<?> property, Integer width, Integer height) {
        input = new Inputfield(property.getValue(), 0, 0, width, height);

        property.addOnValueChangedListener((s, s2) -> {
            if(!input.getTextBox().getText().equals(property.getValue())){
                input.getTextBox().setText(property.getValue());
            }
        });

        return input;
    }


    //endregion
}
