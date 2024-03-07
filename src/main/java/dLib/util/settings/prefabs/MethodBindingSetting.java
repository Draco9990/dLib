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

public class MethodBindingSetting extends CustomSetting<MethodBinding> {
    /** Variables */
    // Dynamic Binding Settings
    private LinkedHashMap<String, Class<?>> params = new LinkedHashMap<>();
    private Class<?> returnType = void.class;
    private String preferredMethodName = "MethodBinding_" + UUID.randomUUID().toString().replace("-", "");

    /** Constructors */
    public MethodBindingSetting(MethodBinding value) {
        super(value);
    }

    public MethodBindingSetting() {
        this(new NoneMethodBinding());
    }

    /** Title override */
    @Override
    public MethodBindingSetting setTitle(String newTitle) {
        super.setTitle(newTitle);
        return this;
    }

    /** Method name */
    public MethodBindingSetting setPreferredMethodName(String methodName){
        this.preferredMethodName = methodName;
        return this;
    }
    public String getPreferredMethodName(){
        return preferredMethodName;
    }

    /** Params */
    public MethodBindingSetting addParameter(String paramName, Class<?> paramType){
        params.put(paramName, paramType);
        return this;
    }
    public MethodBindingSetting setParameters(LinkedHashMap<String, Class<?>> paramsIn){
        this.params = paramsIn;
        return this;
    }
    public LinkedHashMap<String, Class<?>> getParameters(){
        return params;
    }

    /** Return type */
    public MethodBindingSetting setReturnType(Class<?> returnType){
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
    public AbstractSettingUI makeUIFor(int xPos, int yPos, int width, int height) {
        if(getCurrentValue() instanceof DynamicMethodBinding){
            return new DynamicMethodSettingUI(this, xPos, yPos, width, height);
        }
        else{
            return super.makeUIFor(xPos, yPos, width, height);
        }
    }
}
