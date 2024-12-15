package dLib.util.bindings.method;

import dLib.util.bindings.Binding;
import dLib.util.bindings.ResourceBinding;

import java.io.Serializable;

public abstract class MethodBinding extends Binding implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Binding */
    public Object executeBinding(Object target){
        return executeBinding(target, new Object[0]);
    }
    public abstract Object executeBinding(Object target, Object... args);
}
