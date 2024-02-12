package dLib.util.bindings.method;

import dLib.util.bindings.Binding;

import java.io.Serializable;

public abstract class MethodBinding extends Binding  implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Binding */
    public Object executeBinding(Object invoker){
        return executeBinding(null, null);
    }
    public abstract Object executeBinding(Object invoker, Object... args);
}
