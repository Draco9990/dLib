package dLib.util.bindings.method;

import dLib.util.bindings.Binding;

public abstract class MethodBinding extends Binding {
    /** Binding */
    public Object executeBinding(Object invoker){
        return executeBinding(null, null);
    }
    public abstract Object executeBinding(Object invoker, Object... args);
}
