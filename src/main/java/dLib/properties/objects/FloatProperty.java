package dLib.properties.objects;

import dLib.properties.objects.templates.TFloatProperty;

import java.io.Serializable;

public class FloatProperty extends TFloatProperty<FloatProperty> implements Serializable {
    static final long serialVersionUID = 1L;

    public FloatProperty(Float currentValue) {
        super(currentValue);
    }

    public FloatProperty(Float currentValue, Float minimumValue, Float maximumValue) {
        super(currentValue, minimumValue, maximumValue);
    }

    public FloatProperty(Float currentValue, Float minimumValue, Float maximumValue, Float amountOnChange) {
        super(currentValue, minimumValue, maximumValue, amountOnChange);
    }

    public FloatProperty(Float currentValue, Float minimumValue, Float maximumValue, Float incrementAmount, Float decrementAmount) {
        super(currentValue, minimumValue, maximumValue, incrementAmount, decrementAmount);
    }
}
