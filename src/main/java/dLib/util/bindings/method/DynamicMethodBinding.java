package dLib.util.bindings.method;

import dLib.util.Reflection;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DynamicMethodBinding extends MethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Variables */
    private String methodToExecute = "";

    private BiConsumer<String, String> onBoundMethodChangedConsumer;

    /** Constructors */
    public DynamicMethodBinding(String methodName){
        this.methodToExecute = methodName;
    }

    /** Binding */
    public String getBoundMethod(){
        return  methodToExecute;
    }

    public DynamicMethodBinding setBoundMethod(String s){
        String oldMethodToExecute = methodToExecute;
        methodToExecute = s;

        if(onBoundMethodChangedConsumer != null) onBoundMethodChangedConsumer.accept(oldMethodToExecute, methodToExecute);

        return this;
    }

    public DynamicMethodBinding setOnBoundMethodChangedConsumer(BiConsumer<String, String> consumer){
        this.onBoundMethodChangedConsumer = consumer;
        return this;
    }

    /** Validity */

    @Override
    public boolean isValid() {
        return methodToExecute != null && !methodToExecute.isEmpty();
    }

    @Override
    public String getShortDisplayName() {
        return methodToExecute.isEmpty() ? "CUSTOM" : methodToExecute;
    }

    @Override
    public String getFullDisplayName() {
        return getShortDisplayName();
    }

    @Override
    public Object executeBinding(Object invoker, Object... args) {
        if(isValid()){
            return Reflection.invokeMethod(methodToExecute, invoker, args);
        }
        return null;
    }
}
