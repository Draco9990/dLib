package dLib.properties.objects;

import dLib.properties.objects.templates.TProperty;
import dLib.properties.objects.templates.TPropertyArray;

import java.io.Serializable;
import java.util.ArrayList;

public class PropertyArray<ValueType> extends TPropertyArray<ValueType, PropertyArray<ValueType>> implements Serializable {
    static final long serialVersionUID = 1L;

    public PropertyArray(TProperty<ValueType, ?> value) {
        super(value);
    }

    public PropertyArray(ArrayList<? extends TProperty<ValueType, ?>> defaults) {
        super(defaults);
    }
}
