package dLib.properties.objects;

import dLib.properties.objects.templates.TCustomProperty;

import java.io.Serializable;

public abstract class CustomProperty<ItemClass> extends TCustomProperty<ItemClass, CustomProperty<ItemClass>> implements Serializable {
    static final long serialVersionUID = 1L;

    public CustomProperty(ItemClass defaultValue) {
        super(defaultValue);
    }
}
