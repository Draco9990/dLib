package dLib.properties.ui.elements;

import dLib.properties.objects.FloatProperty;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.resources.UICommonResources;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.function.Consumer;

public class FloatValueEditor extends AbstractValueEditor<Float> {
    //region Variables

    public Button leftArrow;
    public Button rightArrow;

    public Inputfield inputbox;

    //endregion

    //region Constructors

    public FloatValueEditor(Float value, AbstractDimension width, AbstractDimension height) {
        super(width, height);

        {
            leftArrow = new Button(Pos.px(0), Pos.px(0), Dim.height(), Dim.fill());
            leftArrow.setImage(UICommonResources.arrow_left);
            leftArrow.onLeftClickEvent.subscribe(this, () -> {
                if(boundProperty instanceof FloatProperty){
                    ((FloatProperty) boundProperty).decrement();
                }
                else{
                    Float currValue = Float.parseFloat(inputbox.getTextBox().getText());
                    setValueEvent.invoke(objectConsumer -> objectConsumer.accept(currValue - 1));
                }
            });
            addChildNCS(leftArrow);

            inputbox = new Inputfield(String.valueOf(value), Pos.perc(0.25), Pos.px(0), Dim.fill(), Dim.fill());
            inputbox.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_DECIMAL);
            inputbox.addOnValueChangedListener(s -> {
                if(boundProperty instanceof FloatProperty){
                    boundProperty.setValueFromString(s);
                }
                else{
                    setValueEvent.invoke(objectConsumer -> objectConsumer.accept(Float.parseFloat(s)));
                }
            });
            addChildNCS(inputbox);

            rightArrow = new Button(Pos.perc(0.75), Pos.px(0), Dim.height(), Dim.fill());
            rightArrow.setImage(UICommonResources.arrow_right);
            rightArrow.onLeftClickEvent.subscribe(this, () -> {
                if(boundProperty instanceof FloatProperty){
                    ((FloatProperty) boundProperty).increment();
                }
                else{
                    Float currValue = Float.parseFloat(inputbox.getTextBox().getText());
                    setValueEvent.invoke(objectConsumer -> objectConsumer.accept(currValue + 1));
                }
            });
            addChildNCS(rightArrow);
        }

        onValueChangedEvent.subscribe(this, (newVal) -> {
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
