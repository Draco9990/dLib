package dLib.util.settings.prefabs;

import dLib.util.settings.NumberSetting;

import java.io.Serializable;

public class IntegerSetting extends NumberSetting<Integer> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */

    /** Constructors */
    public IntegerSetting(Integer currentValue, Integer minimumValue, Integer maximumValue){
        this(currentValue, minimumValue, maximumValue, 1);
    }
    public IntegerSetting(Integer currentValue, Integer minimumValue, Integer maximumValue, Integer amountOnChange){
        this(currentValue, minimumValue, maximumValue, amountOnChange, amountOnChange);
    }
    public IntegerSetting(Integer currentValue, Integer minimumValue, Integer maximumValue, Integer incrementAmount, Integer decrementAmount){
        super(currentValue, minimumValue, maximumValue, incrementAmount, decrementAmount);
    }

    /** Methods */
    @Override
    public void increment() {
        currentValue += incrementAmount;
        if(currentValue > maximumValue){
            currentValue = maximumValue;
        }

        onValueChanged();
    }

    @Override
    public void decrement() {
        currentValue -= decrementAmount;
        if(currentValue < minimumValue){
            currentValue = minimumValue;
        }

        onValueChanged();
    }
}
