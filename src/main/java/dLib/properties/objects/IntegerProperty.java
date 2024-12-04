package dLib.properties.objects;

import dLib.properties.objects.templates.TIntegerProperty;

import java.io.Serializable;

public class IntegerProperty extends TIntegerProperty<IntegerProperty> implements Serializable {
    static final long serialVersionUID = 1L;

    public IntegerProperty(Integer currentValue) {
        super(currentValue);
    }

    public IntegerProperty(Integer currentValue, Integer minimumValue, Integer maximumValue) {
        super(currentValue, minimumValue, maximumValue);
    }

    public IntegerProperty(Integer currentValue, Integer minimumValue, Integer maximumValue, Integer amountOnChange) {
        super(currentValue, minimumValue, maximumValue, amountOnChange);
    }

    public IntegerProperty(Integer currentValue, Integer minimumValue, Integer maximumValue, Integer incrementAmount, Integer decrementAmount) {
        super(currentValue, minimumValue, maximumValue, incrementAmount, decrementAmount);
    }
}
