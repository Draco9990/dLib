package dLib.util.bindings.method;

import dLib.util.Reflection;

public class DynamicMethodBinding extends MethodBinding{
    /** Variables */
    private String methodToExecute = "";

    /** Constructors */
    public DynamicMethodBinding(String methodName){
        this.methodToExecute = methodName;
    }

    @Override
    public boolean isValid() {
        return methodToExecute != null && !methodToExecute.isEmpty();
    }

    @Override
    public String getShortDisplayName() {
        return null;
    }

    @Override
    public String getFullDisplayName() {
        return null;
    }

    @Override
    public Object executeBinding(Object invoker, Object... args) {
        if(isValid()){
            return Reflection.invokeMethod(methodToExecute, invoker, args);
        }
        return null;
    }
}
