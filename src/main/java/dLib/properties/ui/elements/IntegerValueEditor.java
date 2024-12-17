package dLib.properties.ui.elements;

import dLib.properties.objects.IntegerProperty;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.resources.UICommonResources;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class IntegerValueEditor extends AbstractValueEditor<Integer, IntegerProperty> {
    //region Variables

    public Button leftArrow;
    public Button rightArrow;

    public Inputfield inputbox;

    //endregion

    //region Constructors

    public IntegerValueEditor(Integer value, AbstractDimension width, AbstractDimension height){
        this(new IntegerProperty(value), width, height);
    }

    public IntegerValueEditor(IntegerProperty property, AbstractDimension width, AbstractDimension height) {
        super(property);

        {
            leftArrow = new Button(Pos.px(0), Pos.px(0), Dim.height(), Dim.fill());
            leftArrow.setImage(UICommonResources.arrow_left);
            leftArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.decrement());
            addChildNCS(leftArrow);

            inputbox = new Inputfield(property.getValueForDisplay(), Pos.perc(0.25), Pos.px(0), Dim.fill(), Dim.fill());
            inputbox.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            inputbox.addOnValueChangedListener(s -> boundProperty.setValueFromString(s));
            addChildNCS(inputbox);

            rightArrow = new Button(Pos.perc(0.75), Pos.px(0), Dim.height(), Dim.fill());
            rightArrow.setImage(UICommonResources.arrow_right);
            rightArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.increment());
            addChildNCS(rightArrow);
        }

        boundProperty.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
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
