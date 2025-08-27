package dLib.util.bindings.property;

import dLib.properties.objects.templates.TProperty;
import dLib.util.bindings.Binding;

import java.io.Serializable;

public abstract class AbstractPropertyBinding extends Binding<TProperty> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public abstract TProperty resolve(Object... params);
}
