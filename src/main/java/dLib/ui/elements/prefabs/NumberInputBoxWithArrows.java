package dLib.ui.elements.prefabs;

import dLib.properties.objects.NumericProperty;
import dLib.properties.objects.Property;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;

import java.util.function.Consumer;

public class NumberInputBoxWithArrows extends UIElement {
    public Button leftArrow;
    public Button rightArrow;

    public Inputfield inputbox;

    private NumericProperty<?> linkedProperty;

    public NumberInputBoxWithArrows(int xPos, int yPos, int width, int height, ENumberType type) {
        super(xPos, yPos, width, height);

        int arrowDims = height;
        if(arrowDims * 2 > width){
            throw new IllegalArgumentException("Width must be at least twice the height");
        }

        int actualArrowDims = (int) (arrowDims / 2.0f);
        int arrowPadding = (int) (actualArrowDims / 2.0f);
        leftArrow = new Button(arrowPadding, arrowPadding, actualArrowDims, actualArrowDims);
        leftArrow.setImage(UIThemeManager.getDefaultTheme().arrow_left);
        addChildNCS(leftArrow);

        inputbox = new Inputfield("", arrowDims, 0, width - arrowDims * 2, height);
        if(type == ENumberType.WHOLE){
            inputbox.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
        }
        else if(type == ENumberType.DECIMAL){
            inputbox.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_DECIMAL);
        }
        addChildNCS(inputbox);

        rightArrow = new Button(width - arrowDims + arrowPadding, arrowPadding, actualArrowDims, actualArrowDims);
        rightArrow.setImage(UIThemeManager.getDefaultTheme().arrow_right);
        addChildNCS(rightArrow);
    }

    //region Methods

    public NumberInputBoxWithArrows setLinkedProperty(NumericProperty<?> property) {
        inputbox.getTextBox().setText(property.getValue().toString());

        linkedProperty = property;

        leftArrow.addOnLeftClickConsumer(property::decrement);
        leftArrow.addOnLeftClickHeldConsumer(aFloat -> property.decrement());
        rightArrow.addOnLeftClickConsumer(property::increment);
        rightArrow.addOnLeftClickHeldConsumer(aFloat -> property.increment());

        inputbox.addOnValueChangedListener(property::setValueFromString);

        property.addOnValueChangedListener((integer, integer2) -> {
            if(!inputbox.getTextBox().getText().equals(property.getValue().toString())){
                inputbox.getTextBox().setText(property.getValue().toString());
            }
        });

        return this;
    }

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

    public enum ENumberType{
        WHOLE,
        DECIMAL
    }
}
