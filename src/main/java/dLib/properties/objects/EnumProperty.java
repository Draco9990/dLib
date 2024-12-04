package dLib.properties.objects;

import dLib.properties.objects.templates.TEnumProperty;

import java.io.Serializable;

public class EnumProperty<T extends Enum<T>> extends TEnumProperty<T, EnumProperty<T>> implements Serializable {
    static final long serialVersionUID = 1L;

    public EnumProperty(Enum<T> value) {
        super(value);
    }
}
