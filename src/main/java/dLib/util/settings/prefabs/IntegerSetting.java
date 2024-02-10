package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.IntegerArrowSettingUI;
import dLib.util.settings.NumberSetting;
import dLib.util.settings.Setting;

import java.io.Serializable;

public class IntegerSetting extends NumberSetting<Integer> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */

    /** Constructors */
    public IntegerSetting(Integer currentValue){
        this(currentValue, null, null);
    }
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
    public Setting<Integer> setCurrentValue(Integer currentValue) {
        if(maximumValue != null && currentValue > maximumValue) currentValue = maximumValue;
        if(minimumValue != null && currentValue < minimumValue) currentValue = minimumValue;
        return super.setCurrentValue(currentValue);
    }

    @Override
    public void increment() {
        currentValue += incrementAmount;
        if(maximumValue != null && currentValue > maximumValue){
            currentValue = maximumValue;
        }
    }

    @Override
    public void decrement() {
        currentValue -= decrementAmount;
        if(minimumValue != null && currentValue < minimumValue){
            currentValue = minimumValue;
        }
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIFor(int xPos, int yPos, int width, int height) {
        return new IntegerArrowSettingUI(this, xPos, yPos, width, height, false);
    }
}
