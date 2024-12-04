package dLib.properties.objects;

import dLib.properties.objects.templates.TNumericProperty;

import java.io.Serializable;

public abstract class NumericProperty<T extends Number> extends TNumericProperty<T, NumericProperty> implements Serializable {
    static final long serialVersionUID = 1L;

    public NumericProperty(T currentValue) {
        super(currentValue);
    }

    public NumericProperty(T currentValue, T incrementAmount, T decrementAmount) {
        super(currentValue, incrementAmount, decrementAmount);
    }

    public NumericProperty(T currentValue, T minimumValue, T maximumValue, T incrementAmount, T decrementAmount) {
        super(currentValue, minimumValue, maximumValue, incrementAmount, decrementAmount);
    }
}
