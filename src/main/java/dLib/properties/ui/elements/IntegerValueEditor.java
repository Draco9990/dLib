package dLib.properties.ui.elements;

import dLib.properties.objects.IntegerProperty;
import dLib.ui.elements.items.numericaleditors.IntegerInputBox;
import dLib.util.ui.dimensions.Dim;

public class IntegerValueEditor extends AbstractValueEditor<Integer, IntegerProperty> {
    //region Variables

    public IntegerInputBox integerInputBox;

    //endregion

    //region Constructors

    public IntegerValueEditor(Integer value){
        this(new IntegerProperty(value));
    }

    public IntegerValueEditor(IntegerProperty property) {
        super(property);

        integerInputBox = new IntegerInputBox(Dim.fill(), Dim.px(50));

        integerInputBox.leftArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.decrement());
        integerInputBox.leftArrow.onLeftClickHeldEvent.subscribe(this, (heldTime) -> boundProperty.decrement());

        integerInputBox.inputbox.textBox.setText(String.valueOf(boundProperty.getValue()));
        integerInputBox.inputbox.onValueChangedEvent.subscribeManaged(s -> boundProperty.setValueFromString(s));

        integerInputBox.rightArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.increment());
        integerInputBox.rightArrow.onLeftClickHeldEvent.subscribe(this, (heldTime) -> boundProperty.increment());

        addChild(integerInputBox);

        boundProperty.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!integerInputBox.inputbox.textBox.getText().equals(String.valueOf(newVal))){
                integerInputBox.inputbox.textBox.setText(String.valueOf(newVal));
            }
        });
    }

    //endregion
}
