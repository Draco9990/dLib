package dLib.properties.objects;

import dLib.properties.ui.elements.FloatPropertyEditor;

import java.io.Serializable;

public class FloatProperty extends NumericProperty<Float> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public FloatProperty(Float currentValue){
        this(currentValue, null, null);
    }
    public FloatProperty(Float currentValue, Float minimumValue, Float maximumValue){
        this(currentValue, minimumValue, maximumValue, 1.0f);
    }
    public FloatProperty(Float currentValue, Float minimumValue, Float maximumValue, Float amountOnChange){
        this(currentValue, minimumValue, maximumValue, amountOnChange, amountOnChange);
    }
    public FloatProperty(Float currentValue, Float minimumValue, Float maximumValue, Float incrementAmount, Float decrementAmount){
        super(currentValue, minimumValue, maximumValue, incrementAmount, decrementAmount);

        propertyEditorClass = FloatPropertyEditor.class;
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
    public boolean setValueFromString(String value) {
        return setValue(Float.parseFloat(value));
    }

    @Override
    public FloatProperty setName(String newTitle) {
        return (FloatProperty) super.setName(newTitle);
    }

    @Override
    public FloatProperty setDescription(String description) {
        return (FloatProperty) super.setDescription(description);
    }

    @Override
    public FloatProperty setMinimumValue(Float minimumValue) {
        return (FloatProperty) super.setMinimumValue(minimumValue);
    }

    @Override
    public FloatProperty setMaximumValue(Float maximumValue) {
        return (FloatProperty) super.setMaximumValue(maximumValue);
    }

    @Override
    public FloatProperty setLimits(Float minimumValue, Float maximumValue) {
        return (FloatProperty) super.setLimits(minimumValue, maximumValue);
    }

    @Override
    public FloatProperty setIncrementAmount(Float incrementAmount) {
        return (FloatProperty) super.setIncrementAmount(incrementAmount);
    }

    @Override
    public FloatProperty setDecrementAmount(Float decrementAmount) {
        return (FloatProperty) super.setDecrementAmount(decrementAmount);
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
