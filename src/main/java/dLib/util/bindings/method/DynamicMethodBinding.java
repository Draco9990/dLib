package dLib.util.bindings.method;

import dLib.properties.objects.MethodBindingProperty;
import dLib.properties.objects.StringProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.elements.UIElement;
import dLib.util.Reflection;
import dLib.util.bindings.method.editors.DynamicMethodBindingValueEditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class DynamicMethodBinding extends AbstractMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Dynamic Method Binding";
    private static final String PROPERTY_EDITOR_SHORT_NAME = "dyn";

    //region Variables

    private StringProperty methodToExecute = new StringProperty(""){
        @Override
        public boolean isValidValue(String value) {
            if(value.isEmpty() && !getValue().isEmpty()) return false;
            return super.isValidValue(value);
        }
    };

    private transient ArrayList<BiConsumer<String, String>> onBoundMethodChangedConsumers = new ArrayList<>();

    private String methodOwnerId = "";

    //endregion Variables

    /** Constructors */
    public DynamicMethodBinding(String methodName){
        this.methodToExecute.setValue(methodName);
    }

    /** Binding */
    public String getBoundMethod(){
        return methodToExecute.getValue();
    }

    public StringProperty getBoundMethodRaw(){
        return methodToExecute;
    }

    public DynamicMethodBinding setBoundMethod(String methodOwnerId, String s){
        String oldMethodToExecute = methodToExecute.getValue();
        this.methodOwnerId = methodOwnerId;
        if(!methodToExecute.setValue(s)) return this;

        if(onBoundMethodChangedConsumers == null) onBoundMethodChangedConsumers = new ArrayList<>();
        for(BiConsumer<String, String> consumer : onBoundMethodChangedConsumers) consumer.accept(oldMethodToExecute, methodToExecute.getValue());

        return this;
    }

    public DynamicMethodBinding addOnBoundMethodChangedConsumer(BiConsumer<String, String> consumer){
        if(onBoundMethodChangedConsumers == null) onBoundMethodChangedConsumers = new ArrayList<>();
        this.onBoundMethodChangedConsumers.add(consumer);
        return this;
    }

    /** Validity */
    @Override
    public String toString() {
        return methodToExecute.getValue().isEmpty() ? "CUSTOM" : methodToExecute.getValue();
    }

    @Override
    public Object executeBinding(Object target, Object... args) {
        if(target instanceof UIElement){
            //* Dynamic methods are located in the top parent of the UIElement
            target = ((UIElement) target).findParentById(methodOwnerId);
        }

        if(methodToExecute != null && methodToExecute.getValue().isEmpty()){
            return Reflection.invokeMethod(methodToExecute.getValue(), target, args);
        }
        return null;
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new DynamicMethodBindingValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new DynamicMethodBindingValueEditor((MethodBindingProperty) property);
    }
}
