package dLib.util.bindings.property;

import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.IEditableValue;
import dLib.util.bindings.ResourceBinding;

import java.io.Serializable;

public abstract class AbstractPropertyBinding extends ResourceBinding<TProperty> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public abstract TProperty getBoundObject(Object... params);
}
