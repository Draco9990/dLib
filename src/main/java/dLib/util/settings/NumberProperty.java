package dLib.util.settings;

import java.io.Serializable;
import java.util.Objects;

public abstract class NumberProperty<T extends Number> extends Property<T> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private T minimumValue;
    private T maximumValue;

    protected T incrementAmount;
    protected T decrementAmount;

    //endregion

    //region Constructors

    public NumberProperty(T currentValue){
        this(currentValue, null, null, null, null);
    }
    public NumberProperty(T currentValue, T incrementAmount, T decrementAmount){
        this(currentValue, null, null, incrementAmount, decrementAmount);
    }
    public NumberProperty(T currentValue, T minimumValue, T maximumValue, T incrementAmount, T decrementAmount){
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

    @Override
    public boolean isValidValue(T value) {
        if(!super.isValidValue(value)) return false;

        if(maximumValue != null && greaterThan(value, maximumValue)) return false;
        if(minimumValue != null && lessThan(value, minimumValue)) return false;

        return true;
    }

    //endregion

    //region Min & Max values

    public NumberProperty<T> setMinimumValue(T minimumValue){
        return setLimits(minimumValue, maximumValue);
    }
    public NumberProperty<T> setMaximumValue(T maximumValue){
        return setLimits(minimumValue, maximumValue);
    }
    public NumberProperty<T> setLimits(T minimumValue, T maximumValue){
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        return this;
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
