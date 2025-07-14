package dLib.properties.objects;

import dLib.properties.objects.templates.TProperty;

import java.io.Serializable;

public class Property<ValueType> extends TProperty<ValueType, Property<ValueType>> implements Serializable {
    static final long serialVersionUID = 1L;

    public Property(ValueType value) {
        super(value);
    }
}
