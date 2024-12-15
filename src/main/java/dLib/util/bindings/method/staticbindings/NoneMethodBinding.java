package dLib.util.bindings.method.staticbindings;

import java.io.Serializable;

public class NoneMethodBinding extends StaticMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "NONE";

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
