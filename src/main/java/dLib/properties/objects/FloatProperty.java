package dLib.properties.objects;

import dLib.properties.ui.elements.FloatPropertyEditor;
import dLib.properties.ui.elements.IntegerPropertyEditor;

import java.io.Serializable;

public class FloatProperty extends NumberProperty<Float> implements Serializable {
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
    public FloatProperty setName(String newTitle) {
        return (FloatProperty) super.setName(newTitle);
    }

    //endregion
}
