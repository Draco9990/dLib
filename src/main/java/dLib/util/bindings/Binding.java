package dLib.util.bindings;

import java.io.Serializable;

public abstract class Binding implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return getDisplayValue();
    }

    public abstract String getDisplayValue();
}
