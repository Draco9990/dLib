package dLib.properties.ui.elements;

import dLib.modcompat.ModManager;
import dLib.properties.objects.templates.TProperty;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.util.bindings.string.Str;
import dLib.util.ui.dimensions.Dim;

public class PropertyOverridenValueEditor extends AbstractValueEditor<TProperty> {
    //region Variables

    TextButton enumBox;

    //endregion

    //region Constructors

    public PropertyOverridenValueEditor(TProperty<?, ?> property) {
        super(property);

        HorizontalBox box = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            enumBox = new TextButton("Edit", Dim.fill(), Dim.px(50));
            enumBox.setSayTheSpireElementName(Str.lambda(property::getName));
            enumBox.setSayTheSpireElementValue(Str.lambda(property::getValueForDisplay));
            enumBox.postLeftClickEvent.subscribe(PropertyOverridenValueEditor.this, () -> boundProperty.getCustomEditorOverride().accept(boundProperty));
            box.addChild(enumBox);
        }
        addChild(box);

        setControllerSelectable(true);

        property.onValueChangedEvent.subscribe(this, (oldValue, newValue) -> {
            if(!isEditorValidForPropertyChange()) return;

            ModManager.SayTheSpire.outputCond(boundProperty.getName() + " value changed to " + boundProperty.getValueForDisplay());
        });
    }

    //endregion
}
