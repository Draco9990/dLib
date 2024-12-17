package dLib.properties.objects.templates;

import dLib.util.ui.dimensions.AbstractDimension;

public class TDimensionProperty<PropertyType> extends TProperty<AbstractDimension, PropertyType>{
    public TDimensionProperty(AbstractDimension value) {
        super(value);
    }

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("PositionProperty does not support setting value from string");
    }
}
