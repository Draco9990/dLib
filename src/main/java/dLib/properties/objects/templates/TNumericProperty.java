package dLib.properties.objects.templates;

import java.io.Serializable;
import java.util.Objects;

public abstract class TNumericProperty<T extends Number, PropertyType> extends TProperty<T, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private T minimumValue;
    private T maximumValue;

    protected T incrementAmount;
    protected T decrementAmount;

    //endregion

    //region Constructors

    public TNumericProperty(T currentValue){
        this(currentValue, null, null, null, null);
    }
    public TNumericProperty(T currentValue, T incrementAmount, T decrementAmount){
        this(currentValue, null, null, incrementAmount, decrementAmount);
    }
    public TNumericProperty(T currentValue, T minimumValue, T maximumValue, T incrementAmount, T decrementAmount){
        super(currentValue);

        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;

        this.incrementAmount = incrementAmount;
        this.decrementAmount = decrementAmount;
    }

    //endregion

    //region Methods

    //region Value

    public void increment(){
        if(incrementAmount != null){
            setValue(add(getValue(), incrementAmount));
        }
    }
    public void decrement(){
        if(decrementAmount != null){
            setValue(subtract(getValue(), decrementAmount));
        }
    }

    public PropertyType setIncrementAmount(T incrementAmount){
        this.incrementAmount = incrementAmount;
        return (PropertyType) this;
    }

    public T getIncrementAmount(){
        return incrementAmount;
    }

    public PropertyType setDecrementAmount(T decrementAmount){
        this.decrementAmount = decrementAmount;
        return (PropertyType) this;
    }

    public T getDecrementAmount(){
        return decrementAmount;
    }

    @Override
    public boolean isValidValue(T value) {
        if(!super.isValidValue(value)) return false;

        if(maximumValue != null && greaterThan(value, maximumValue)) return false;
        if(minimumValue != null && lessThan(value, minimumValue)) return false;

        return true;
    }

    //endregion

    //region Min & Max values

    public PropertyType setMinimumValue(T minimumValue){
        return (PropertyType) setLimits(minimumValue, maximumValue);
    }
    public PropertyType setMaximumValue(T maximumValue){
        return (PropertyType) setLimits(minimumValue, maximumValue);
    }
    public PropertyType setLimits(T minimumValue, T maximumValue){
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;

        ensureValueWithinLimits();

        return (PropertyType) this;
    }

    public void ensureValueWithinLimits(){
        T desiredValue = getValue();
        if(maximumValue != null && greaterThan(desiredValue, maximumValue)){
            desiredValue = maximumValue;
        }
        if(minimumValue != null && lessThan(desiredValue, minimumValue)){
            desiredValue = minimumValue;
        }

        if(!Objects.equals(getValue(), desiredValue)){
            setValue(desiredValue);
        }
    }

    //endregion

    //region Operations

    protected abstract boolean greaterThan(T lhs, T rhs);
    protected abstract boolean lessThan(T lhs, T rhs);

    protected abstract T add(T lhs, T rhs);
    protected abstract T subtract(T lhs, T rhs);
    protected abstract T divide(T lhs, T rhs);
    protected abstract T multiply(T lhs, T rhs);

    //endregion

    //endregion
}
