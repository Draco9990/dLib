package dLib.properties.objects.templates;

import dLib.properties.ui.elements.IntegerValueEditor;

import java.io.Serializable;

public abstract class TIntegerProperty<PropertyType> extends TNumericProperty<Integer, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public TIntegerProperty(Integer currentValue){
        this(currentValue, null, null);
    }
    public TIntegerProperty(Integer currentValue, Integer minimumValue, Integer maximumValue){
        this(currentValue, minimumValue, maximumValue, 1);
    }
    public TIntegerProperty(Integer currentValue, Integer minimumValue, Integer maximumValue, Integer amountOnChange){
        this(currentValue, minimumValue, maximumValue, amountOnChange, amountOnChange);
    }
    public TIntegerProperty(Integer currentValue, Integer minimumValue, Integer maximumValue, Integer incrementAmount, Integer decrementAmount){
        super(currentValue, minimumValue, maximumValue, incrementAmount, decrementAmount);
    }

    //endregion

    //region Methods

    //region Operators

    @Override
    protected boolean greaterThan(Integer lhs, Integer rhs) {
        return lhs > rhs;
    }

    @Override
    protected boolean lessThan(Integer lhs, Integer rhs) {
        return lhs < rhs;
    }

    @Override
    protected Integer add(Integer lhs, Integer rhs) {
        return lhs + rhs;
    }

    @Override
    protected Integer subtract(Integer lhs, Integer rhs) {
        return lhs - rhs;
    }

    @Override
    protected Integer divide(Integer lhs, Integer rhs) {
        return lhs / rhs;
    }

    @Override
    protected Integer multiply(Integer lhs, Integer rhs) {
        return lhs * rhs;
    }


    //endregion

    @Override
    public boolean setValueFromString(String value) {
        return setValue(Integer.parseInt(value));
    }

    //endregion
}
