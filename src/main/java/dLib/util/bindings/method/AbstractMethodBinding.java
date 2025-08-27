package dLib.util.bindings.method;

import dLib.util.bindings.Binding;

import java.io.Serializable;

public abstract class AbstractMethodBinding extends Binding<Object> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Binding */
    public Object resolve(Object target){
        return resolve(target, new Object[0]);
    }

    @Override
    public Object resolve(Object... params) {
        return resolve(params[0], params.length > 1 ? java.util.Arrays.copyOfRange(params, 1, params.length) : new Object[0]);
    }

    protected abstract Object resolve(Object target, Object... args);
}
