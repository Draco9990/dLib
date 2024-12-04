package dLib.properties.objects;

import dLib.properties.objects.templates.TStringProperty;

import java.io.Serializable;

public class StringProperty extends TStringProperty<StringProperty> implements Serializable {
    static final long serialVersionUID = 1L;

    public StringProperty(String defaultValue) {
        super(defaultValue);
    }
}
