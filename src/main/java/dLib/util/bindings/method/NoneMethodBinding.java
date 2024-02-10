package dLib.util.bindings.method;

public class NoneMethodBinding extends MethodBinding{
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
