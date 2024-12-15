package dLib.util.bindings;

public abstract class ResourceBinding<ResourceType> extends Binding{

    public abstract ResourceType getBoundObject(Object... params);
}
