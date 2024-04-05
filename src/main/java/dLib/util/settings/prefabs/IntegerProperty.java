package dLib.util.settings.prefabs;

import dLib.propertyeditors.ui.elements.AbstractPropertyEditor;
import dLib.propertyeditors.ui.elements.IntegerPropertyEditor;
import dLib.util.settings.NumberProperty;

import java.io.Serializable;

public class IntegerProperty extends NumberProperty<Integer> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public IntegerProperty(Integer currentValue){
        this(currentValue, null, null);
    }
    public IntegerProperty(Integer currentValue, Integer minimumValue, Integer maximumValue){
        this(currentValue, minimumValue, maximumValue, 1);
    }
    public IntegerProperty(Integer currentValue, Integer minimumValue, Integer maximumValue, Integer amountOnChange){
        this(currentValue, minimumValue, maximumValue, amountOnChange, amountOnChange);
    }
    public IntegerProperty(Integer currentValue, Integer minimumValue, Integer maximumValue, Integer incrementAmount, Integer decrementAmount){
        super(currentValue, minimumValue, maximumValue, incrementAmount, decrementAmount);

        propertyEditorClass = IntegerPropertyEditor.class;
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
    public IntegerProperty setName(String newTitle) {
        return (IntegerProperty) super.setName(newTitle);
    }

    //endregion
}
