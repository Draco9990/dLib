package dLib.properties.objects.templates;

import basemod.Pair;
import dLib.properties.ui.elements.MethodBindingPropertyEditor;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.MethodBindingHelpers;
import dLib.util.bindings.method.NoneMethodBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

public abstract class TMethodBindingProperty<PropertyType> extends TCustomProperty<MethodBinding, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private Class<?> dynamic_returnType = void.class;
    private String dynamic_methodName = "MethodBinding_" + UUID.randomUUID().toString().replace("-", "");
    private LinkedHashMap<String, Class<?>> dynamic_params = new LinkedHashMap<>();
    private String dynamic_methodBody = "{\n//IMPLEMENTATION HERE\n}";

    private boolean dynamicBindingOnly = false;
    private boolean noDynamicBinding = false;

    //endregion

    //region Constructors

    public TMethodBindingProperty(MethodBinding value) {
        super(value);

        propertyEditorClass = MethodBindingPropertyEditor.class;
    }

    public TMethodBindingProperty() {
        this(new NoneMethodBinding());
    }

    //endregion

    //region Methods

    //region Values

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("MethodBindingProperty does not support setting values from strings");
    }

    @Override
    public boolean isValidValue(MethodBinding value) {
        if(!super.isValidValue(value)) return false;

        if(dynamicBindingOnly && !(value instanceof DynamicMethodBinding)){
            return false;
        }
        if(noDynamicBinding && value instanceof DynamicMethodBinding){
            return false;
        }

        return true;
    }


    //endregion

    //region Dynamic Method Creation

    public TMethodBindingProperty setDynamicCreationReturnType(Class<?> returnType){
        this.dynamic_returnType = returnType;
        return this;
    }
    public Class<?> getDynamicCreationReturnType(){
        return dynamic_returnType;
    }

    public PropertyType setDynamicCreationMethodName(String methodName){
        this.dynamic_methodName = methodName;
        return (PropertyType) this;
    }
    public String getDynamicCreationMethodName(){
        return dynamic_methodName;
    }

    public PropertyType addDynamicCreationParameter(String paramName, Class<?> paramType){
        dynamic_params.put(paramName, paramType);
        return (PropertyType) this;
    }
    public PropertyType setDynamicCreationParameters(LinkedHashMap<String, Class<?>> paramsIn){
        this.dynamic_params = paramsIn;
        return (PropertyType) this;
    }
    @SafeVarargs
    public final PropertyType setDynamicCreationParameters(Pair<String, Class<?>>... params){
        for(Pair<String, Class<?>> param : params){
            dynamic_params.put(param.getKey(), param.getValue());
        }
        return (PropertyType) this;
    }
    public LinkedHashMap<String, Class<?>> getDynamicCreationParameters(){
        return dynamic_params;
    }

    public PropertyType setDynamicCreationMethodBody(String body){
        this.dynamic_methodBody = body;
        return (PropertyType) this;
    }
    public String getDynamicCreationMethodBody(){
        return dynamic_methodBody;
    }

    //endregion

    //region Dynamic Method Binding

    public TMethodBindingProperty setDynamicBindingsOnly(){
        dynamicBindingOnly = true;
        noDynamicBinding = false;
        if(!(getValue() instanceof DynamicMethodBinding)){
            setValue(new DynamicMethodBinding(""));
        }
        return this;
    }

    public TMethodBindingProperty disableDynamicBindings(){
        dynamicBindingOnly = false;
        noDynamicBinding = true;
        if(getValue() instanceof DynamicMethodBinding){
            setValue(new NoneMethodBinding());
        }
        return this;
    }

    //endregion

    @Override
    public ArrayList<MethodBinding> getAllOptions() {
        return MethodBindingHelpers.getPremadeMethodBindings();
    }

    //endregion
}
