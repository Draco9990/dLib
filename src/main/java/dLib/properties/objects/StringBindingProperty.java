package dLib.properties.objects;

import dLib.properties.objects.templates.TStringBindingProperty;
import dLib.util.bindings.string.AbstractStringBinding;

import java.io.Serializable;

public class StringBindingProperty extends TStringBindingProperty<StringBindingProperty> implements Serializable {
    static final long serialVersionUID = 1L;

    public StringBindingProperty(AbstractStringBinding currentValue) {
        super(currentValue);
    }

    public StringBindingProperty(){
        super();
    }
}
