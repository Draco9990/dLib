package dLib.properties.objects;

import dLib.properties.objects.templates.TBooleanProperty;

import java.io.Serializable;

public class BooleanProperty extends TBooleanProperty<BooleanProperty> implements Serializable {
    static final long serialVersionUID = 1L;

    public BooleanProperty(boolean defaultValue) {
        super(defaultValue);
    }
}
