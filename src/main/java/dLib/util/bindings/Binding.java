package dLib.util.bindings;

import dLib.properties.ui.elements.IEditableValue;

import java.io.Serializable;

public abstract class Binding<ResourceType>  implements Serializable, IEditableValue {
    private static final long serialVersionUID = 1L;

    public abstract ResourceType resolve(Object... params);
}
