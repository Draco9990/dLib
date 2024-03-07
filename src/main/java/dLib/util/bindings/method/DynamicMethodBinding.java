package dLib.util.bindings.method;

import dLib.util.Reflection;
import dLib.util.settings.prefabs.StringSetting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class DynamicMethodBinding extends MethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Variables */
    private StringSetting methodToExecute = new StringSetting(""){
        @Override
        public boolean isValidValue(String value) {
            if(value.isEmpty() && !getCurrentValue().isEmpty()) return false;
            return super.isValidValue(value);
        }
    };

    private transient ArrayList<BiConsumer<String, String>> onBoundMethodChangedConsumers = new ArrayList<>();

    /** Constructors */
    public DynamicMethodBinding(String methodName){
        this.methodToExecute.trySetValue(methodName);
    }

    /** Binding */
    public String getBoundMethod(){
        return methodToExecute.getCurrentValue();
    }

    public StringSetting getBoundMethodSetting(){
        return methodToExecute;
    }

    public DynamicMethodBinding setBoundMethod(String s){
        String oldMethodToExecute = methodToExecute.getCurrentValue();
        if(!methodToExecute.trySetValue(s)) return this;

        for(BiConsumer<String, String> consumer : onBoundMethodChangedConsumers) consumer.accept(oldMethodToExecute, methodToExecute.getCurrentValue());

        return this;
    }

    public DynamicMethodBinding addOnBoundMethodChangedConsumer(BiConsumer<String, String> consumer){
        this.onBoundMethodChangedConsumers.add(consumer);
        return this;
    }

    /** Validity */

    @Override
    public boolean isValid() {
        return methodToExecute != null && !methodToExecute.getCurrentValue().isEmpty();
    }

    @Override
    public String getShortDisplayName() {
        return methodToExecute.getCurrentValue().isEmpty() ? "CUSTOM" : methodToExecute.getCurrentValue();
    }

    @Override
    public String getFullDisplayName() {
        return getShortDisplayName();
    }

    @Override
    public Object executeBinding(Object target, Object... args) {
        if(isValid()){
            return Reflection.invokeMethod(methodToExecute.getCurrentValue(), target, args);
        }
        return null;
    }
}
