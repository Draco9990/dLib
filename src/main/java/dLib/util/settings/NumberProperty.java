package dLib.util.settings;

import java.io.Serializable;

public abstract class NumberProperty<T extends Number> extends Property<T> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */
    protected T minimumValue;
    protected T maximumValue;

    protected T incrementAmount;
    protected T decrementAmount;

    /** Constructors */
    public NumberProperty(T currentValue, T minimumValue, T maximumValue, T incrementAmount, T decrementAmount){
        super(currentValue);

        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;

        this.incrementAmount = incrementAmount;
        this.decrementAmount = decrementAmount;
    }

    /** Bounds */
    public NumberProperty<T> setBounds(T minimumValue, T maximumValue){
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        return this;
    }
    /** Getters and Setters */
    public T getMinimumValue(){
        return minimumValue;
    }

    public T getMaximumValue(){
        return maximumValue;
    }

    /** Methods */
    public abstract void increment();
    public abstract void decrement();
}
