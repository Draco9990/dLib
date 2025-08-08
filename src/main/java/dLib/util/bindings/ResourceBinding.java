package dLib.util.bindings;

import java.io.Serializable;

public abstract class ResourceBinding<ResourceType> extends Binding implements Serializable {

    public abstract ResourceType resolve(Object... params);
}
