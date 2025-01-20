package dLib.properties.ui.elements;

import dLib.properties.objects.FloatProperty;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.numericaleditors.FloatInputBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

public class FloatValueEditor extends AbstractValueEditor<Float, FloatProperty> {
    //region Variables

    public FloatInputBox floatInputBox;

    //endregion

    //region Constructors

    public FloatValueEditor(Float value){
        this(new FloatProperty(value));
    }

    public FloatValueEditor(FloatProperty property) {
        super(property);

        floatInputBox = new FloatInputBox(Dim.fill(), Dim.px(50));
        floatInputBox.leftArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.decrement());
        floatInputBox.leftArrow.onLeftClickHeldEvent.subscribe(this, (heldTime) -> boundProperty.decrement());

        floatInputBox.inputbox.textBox.setText(boundProperty.getValueForDisplay());
        floatInputBox.inputbox.onValueChangedEvent.subscribeManaged(s -> boundProperty.setValueFromString(s));

        floatInputBox.rightArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.increment());
        floatInputBox.rightArrow.onLeftClickHeldEvent.subscribe(this, (heldTime) -> boundProperty.increment());

        addChild(floatInputBox);

        property.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!floatInputBox.inputbox.textBox.getText().equals(String.valueOf(newVal))){
                floatInputBox.inputbox.textBox.setText(String.valueOf(newVal));
            }
        });
    }

    //endregion
}
