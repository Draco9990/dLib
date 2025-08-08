package dLib.util.bindings.string;

import dLib.util.bindings.ResourceBinding;

import java.io.Serializable;

public abstract class AbstractStringBinding extends ResourceBinding<String> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public abstract String resolve(Object... params);
}
