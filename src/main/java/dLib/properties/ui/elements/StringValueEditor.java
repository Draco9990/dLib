package dLib.properties.ui.elements;

import dLib.modcompat.ModManager;
import dLib.modcompat.saythespire.SayTheSpireIntegration;
import dLib.properties.objects.FloatProperty;
import dLib.properties.objects.StringProperty;
import dLib.ui.elements.items.input.Inputfield;
import dLib.util.bindings.string.Str;
import dLib.util.ui.dimensions.Dim;

public abstract class StringValueEditor extends AbstractValueEditor<String, StringProperty> {
    //region Variables

    protected Inputfield inputfield;

    //endregion

    //region Constructors

    public StringValueEditor(String value){
        this(new StringProperty(value));
    }

    public StringValueEditor(StringProperty property) {
        super(property);

        inputfield = new Inputfield(property.getValueForDisplay(), Dim.fill(), Dim.px(50));
        inputfield.setSayTheSpireElementName(Str.lambda(property::getName));
        inputfield.setSayTheSpireElementValue(Str.lambda(property::getValueForDisplay));

        property.onValueChangedEvent.subscribe(this, (oldVal, newValue) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputfield.textBox.getText().equals(newValue)){
                inputfield.textBox.setText(newValue);
            }

            if(ModManager.SayTheSpire.isActive()){
                SayTheSpireIntegration.Output(boundProperty.getName() + " value changed to " + boundProperty.getValueForDisplay());
            }
        });

        addChild(inputfield);
    }


    //endregion
}
