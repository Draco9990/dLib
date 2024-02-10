package dLib.util.bindings;

public abstract class Binding {
    /** Binding */
    public abstract boolean isValid();

    /** To String */
    @Override
    public String toString() {
        return getShortDisplayName();
    }

    /** Display */
    public abstract String getShortDisplayName();
    public abstract String getFullDisplayName();

}
