package dLib.properties.objects;

import dLib.properties.objects.templates.TMethodBindingProperty;
import dLib.util.bindings.method.MethodBinding;

import java.io.Serializable;

public class MethodBindingProperty extends TMethodBindingProperty<MethodBindingProperty> implements Serializable {
    static final long serialVersionUID = 1L;

    public MethodBindingProperty(MethodBinding value) {
        super(value);
    }

    public MethodBindingProperty(){
        super();
    }
}
