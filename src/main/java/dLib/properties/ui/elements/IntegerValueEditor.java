package dLib.properties.ui.elements;

import dLib.properties.objects.IntegerProperty;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class IntegerValueEditor extends AbstractValueEditor<Integer, IntegerProperty> {
    //region Variables

    public Button leftArrow;
    public Button rightArrow;

    public Inputfield inputbox;

    //endregion

    //region Constructors

    public IntegerValueEditor(Integer value){
        this(new IntegerProperty(value));
    }

    public IntegerValueEditor(IntegerProperty property) {
        super(property);

        HorizontalBox box = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(50));
        {
            leftArrow = new Button(Dim.mirror(), Dim.px(50));
            leftArrow.setImage(Tex.stat(UICommonResources.arrow_left));
            leftArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.decrement());
            leftArrow.onLeftClickHeldEvent.subscribe(this, (heldTime) -> boundProperty.decrement());
            leftArrow.setControllerSelectable(false);
            box.addItem(leftArrow);

            inputbox = new Inputfield(property.getValueForDisplay(), Dim.fill(), Dim.px(50));
            inputbox.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            inputbox.onValueChangedEvent.subscribeManaged(s -> boundProperty.setValueFromString(s));
            box.addItem(inputbox);

            rightArrow = new Button(Dim.mirror(), Dim.px(50));
            rightArrow.setImage(Tex.stat(UICommonResources.arrow_right));
            rightArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.increment());
            rightArrow.onLeftClickHeldEvent.subscribe(this, (heldTime) -> boundProperty.increment());
            rightArrow.setControllerSelectable(false);
            box.addItem(rightArrow);
        }
        addChild(box);

        setControllerSelectable(true);

        boundProperty.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputbox.textBox.getText().equals(String.valueOf(newVal))){
                inputbox.textBox.setText(String.valueOf(newVal));
            }
        });
    }

    //endregion

    //region Methods

    @Override
    public boolean onLeftInteraction() {
        return leftArrow.onConfirmInteraction();
    }

    @Override
    public boolean onRightInteraction() {
        return rightArrow.onConfirmInteraction();
    }

    @Override
    public boolean onConfirmInteraction() {
        return inputbox.onConfirmInteraction();
    }

    //endregion
}
