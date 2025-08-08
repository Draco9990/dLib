package dLib.properties.objects.templates;

import java.io.Serializable;

public abstract class TFloatProperty<PropertyType> extends TNumericProperty<Float, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public TFloatProperty(Float currentValue){
        this(currentValue, null, null);
    }
    public TFloatProperty(Float currentValue, Float minimumValue, Float maximumValue){
        this(currentValue, minimumValue, maximumValue, 1.0f);
    }
    public TFloatProperty(Float currentValue, Float minimumValue, Float maximumValue, Float amountOnChange){
        this(currentValue, minimumValue, maximumValue, amountOnChange, amountOnChange);
    }
    public TFloatProperty(Float currentValue, Float minimumValue, Float maximumValue, Float incrementAmount, Float decrementAmount){
        super(currentValue, minimumValue, maximumValue, incrementAmount, decrementAmount);
    }

    //endregion

    //region Methods

    //region Operators

    @Override
    protected boolean greaterThan(Float lhs, Float rhs) {
        return lhs > rhs;
    }

    @Override
    protected boolean lessThan(Float lhs, Float rhs) {
        return lhs < rhs;
    }

    @Override
    protected Float add(Float lhs, Float rhs) {
        return lhs + rhs;
    }

    @Override
    protected Float subtract(Float lhs, Float rhs) {
        return lhs - rhs;
    }

    @Override
    protected Float divide(Float lhs, Float rhs) {
        return lhs / rhs;
    }

    @Override
    protected Float multiply(Float lhs, Float rhs) {
        return lhs * rhs;
    }


    //endregion


    @Override
    public Float sanitizeValue(Float newValue) {
        return (float) Math.round(newValue * 100) / 100;
    }

    @Override
    public boolean setValueFromString(String value) {
        return setValue(Float.parseFloat(value));
    }

    @Override
    public String valueToString() {
        return value.toString();
    }

    @Override
    public void increment() {
        setValue(add(getValue(), incrementAmount));
    }

    @Override
    public void decrement() {
        setValue(subtract(getValue(), decrementAmount));
    }

    //endregion
}
