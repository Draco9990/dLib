package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.IntegerSettingUI;
import dLib.util.settings.NumberProperty;
import dLib.util.settings.Property;

import java.io.Serializable;

public class IntegerProperty extends NumberProperty<Integer> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */

    /** Constructors */
    public IntegerProperty(Integer currentValue){
        this(currentValue, null, null);
    }
    public IntegerProperty(Integer currentValue, Integer minimumValue, Integer maximumValue){
        this(currentValue, minimumValue, maximumValue, 1);
    }
    public IntegerProperty(Integer currentValue, Integer minimumValue, Integer maximumValue, Integer amountOnChange){
        this(currentValue, minimumValue, maximumValue, amountOnChange, amountOnChange);
    }
    public IntegerProperty(Integer currentValue, Integer minimumValue, Integer maximumValue, Integer incrementAmount, Integer decrementAmount){
        super(currentValue, minimumValue, maximumValue, incrementAmount, decrementAmount);
    }

    /** Methods */
    @Override
    public Property<Integer> setValue_internal(Integer value) {
        if(maximumValue != null && value > maximumValue) value = maximumValue;
        if(minimumValue != null && value < minimumValue) value = minimumValue;
        return super.setValue_internal(value);
    }

    @Override
    public void increment() {
        value += incrementAmount;
        if(maximumValue != null && value > maximumValue){
            value = maximumValue;
        }
    }

    @Override
    public void decrement() {
        value -= decrementAmount;
        if(minimumValue != null && value < minimumValue){
            value = minimumValue;
        }
    }

    /** Title */
    @Override
    public IntegerProperty setName(String newTitle) {
        return (IntegerProperty) super.setName(newTitle);
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIForEdit(int xPos, int yPos, int width, int height) {
        return new IntegerSettingUI(this, xPos, yPos, width, height, false);
    }
}
