package dLib.util.bindings.method;

import java.io.Serializable;

public class NoneMethodBinding extends MethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Binding */
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Object executeBinding(Object invoker, Object... args) {
        return null;
    }

    /** Name */
    @Override
    public String getShortDisplayName() {
        return "NONE";
    }

    @Override
    public String getFullDisplayName() {
        return getShortDisplayName();
    }
}
