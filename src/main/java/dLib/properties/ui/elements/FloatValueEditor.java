package dLib.properties.ui.elements;

import dLib.properties.objects.FloatProperty;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class FloatValueEditor extends AbstractValueEditor<Float, FloatProperty> {
    //region Variables

    public Button leftArrow;
    public Button rightArrow;

    public Inputfield inputbox;

    //endregion

    //region Constructors

    public FloatValueEditor(Float value){
        this(new FloatProperty(value));
    }

    public FloatValueEditor(FloatProperty property) {
        super(property);

        HorizontalBox hBox = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            leftArrow = new Button(Dim.height(), Dim.px(50));
            leftArrow.setImage(Tex.stat(UICommonResources.arrow_left));
            leftArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.decrement());
            leftArrow.onLeftClickHeldEvent.subscribe(this, (heldTime) -> boundProperty.decrement());
            hBox.addItem(leftArrow);

            inputbox = new Inputfield(boundProperty.getValueForDisplay(), Dim.fill(), Dim.px(50));
            inputbox.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_DECIMAL_POSITIVE);
            inputbox.addOnValueChangedListener(s -> boundProperty.setValueFromString(s));
            hBox.addItem(inputbox);

            rightArrow = new Button(Dim.height(), Dim.px(50));
            rightArrow.setImage(Tex.stat(UICommonResources.arrow_right));
            rightArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.increment());
            rightArrow.onLeftClickHeldEvent.subscribe(this, (heldTime) -> boundProperty.increment());
            hBox.addItem(rightArrow);
        }
        addChildNCS(hBox);

        property.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputbox.getTextBox().getText().equals(String.valueOf(newVal))){
                inputbox.getTextBox().setText(String.valueOf(newVal));
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
