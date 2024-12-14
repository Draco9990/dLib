package dLib.util.bindings.method;

import java.io.Serializable;

public abstract class StaticMethodBinding extends MethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Static Method Binding";
    private static final String PROPERTY_EDITOR_SHORT_NAME = "stat";

}
