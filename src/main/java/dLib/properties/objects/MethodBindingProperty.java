package dLib.properties.objects;

import dLib.properties.objects.templates.TMethodBindingProperty;
import dLib.util.bindings.method.AbstractMethodBinding;

import java.io.Serializable;

public class MethodBindingProperty extends TMethodBindingProperty<MethodBindingProperty> implements Serializable {
    static final long serialVersionUID = 1L;

    public MethodBindingProperty(AbstractMethodBinding value) {
        super(value);
    }

    public MethodBindingProperty(){
        super();
    }
}
