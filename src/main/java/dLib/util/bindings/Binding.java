package dLib.util.bindings;

import java.io.Serializable;

public abstract class Binding  implements Serializable {
    private static final long serialVersionUID = 1L;

    /** To String */
    @Override
    public String toString() {
        return getShortDisplayName();
    }

    /** Display */
    public abstract String getShortDisplayName();
    public abstract String getFullDisplayName();

}
