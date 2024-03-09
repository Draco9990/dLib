package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.DynamicMethodSettingUI;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.MethodBindingHelpers;
import dLib.util.bindings.method.NoneMethodBinding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

public class MethodBindingProperty extends CustomProperty<MethodBinding> {
    /** Variables */
    // Dynamic Binding Settings
    private LinkedHashMap<String, Class<?>> params = new LinkedHashMap<>();
    private Class<?> returnType = void.class;
    private String preferredMethodName = "MethodBinding_" + UUID.randomUUID().toString().replace("-", "");

    /** Constructors */
    public MethodBindingProperty(MethodBinding value) {
        super(value);
    }

    public MethodBindingProperty() {
        this(new NoneMethodBinding());
    }

    /** Title override */
    @Override
    public MethodBindingProperty setName(String newTitle) {
        super.setName(newTitle);
        return this;
    }

    /** Method name */
    public MethodBindingProperty setPreferredMethodName(String methodName){
        this.preferredMethodName = methodName;
        return this;
    }
    public String getPreferredMethodName(){
        return preferredMethodName;
    }

    /** Params */
    public MethodBindingProperty addParameter(String paramName, Class<?> paramType){
        params.put(paramName, paramType);
        return this;
    }
    public MethodBindingProperty setParameters(LinkedHashMap<String, Class<?>> paramsIn){
        this.params = paramsIn;
        return this;
    }
    public LinkedHashMap<String, Class<?>> getParameters(){
        return params;
    }

    /** Return type */
    public MethodBindingProperty setReturnType(Class<?> returnType){
        this.returnType = returnType;
        return this;
    }
    public Class<?> getReturnType(){
        return returnType;
    }

    /** All options */
    @Override
    public ArrayList<MethodBinding> getAllOptions() {
        return MethodBindingHelpers.getPremadeMethodBindings();
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIForEdit(int xPos, int yPos, int width, int height) {
        if(getValue() instanceof DynamicMethodBinding){
            return new DynamicMethodSettingUI(this, xPos, yPos, width, height);
        }
        else{
            return super.makeUIForEdit(xPos, yPos, width, height);
        }
    }
}
