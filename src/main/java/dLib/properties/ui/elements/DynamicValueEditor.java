package dLib.properties.ui.elements;

import dLib.modcompat.ModManager;
import dLib.properties.objects.DynamicProperty;
import dLib.properties.objects.EnumProperty;
import dLib.ui.elements.items.ComboBox;
import dLib.util.bindings.string.Str;
import dLib.util.helpers.EnumHelpers;
import dLib.util.ui.dimensions.Dim;

import java.util.function.Consumer;

public class DynamicValueEditor<OfType> extends AbstractValueEditor<OfType, DynamicProperty<OfType>> {
    //region Variables

    ComboBox<OfType> comboBox;

    //endregion

    //region Constructors

    public DynamicValueEditor(DynamicProperty<OfType> property) {
        super(property);

        comboBox = new ComboBox<OfType>(boundProperty.getValue(), property.getChoices(), Dim.fill(), Dim.px(50));
        comboBox.onSelectionChangedEvent.subscribe(this, (newVal) -> boundProperty.setValue(newVal));
        comboBox.setSayTheSpireElementName(Str.lambda(property::getName));
        comboBox.setSayTheSpireElementValue(Str.lambda(property::getValueForDisplay));
        addChild(comboBox);

        property.onValueChangedEvent.subscribe(this, (oldValue, newValue) -> {
            if(!isEditorValidForPropertyChange()) return;

            comboBox.label.setText(boundProperty.getValueForDisplay());

            ModManager.SayTheSpire.outputCond(boundProperty.getName() + " value changed to " + boundProperty.getValueForDisplay());
        });
    }

    //endregion
}
