package dLib.properties.objects.templates;

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

    private Class<?> dnc_returnType = void.class;
    private String dnc_methodName = "MethodBinding_" + UUID.randomUUID().toString().replace("-", "");
    private LinkedHashMap<String, Class<?>> dnc_params = new LinkedHashMap<>();
    private String dnc_methodBody = "{\n//IMPLEMENTATION HERE\n}";

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

    public TMethodBindingProperty setDNCReturnType(Class<?> returnType){
        this.dnc_returnType = returnType;
        return this;
    }
    public Class<?> getDNCReturnType(){
        return dnc_returnType;
    }

    public PropertyType setDNCMethodName(String methodName){
        this.dnc_methodName = methodName;
        return (PropertyType) this;
    }
    public String getDNCMethodName(){
        return dnc_methodName;
    }

    public PropertyType addDNCParameter(String paramName, Class<?> paramType){
        dnc_params.put(paramName, paramType);
        return (PropertyType) this;
    }
    public PropertyType setDNCParameters(LinkedHashMap<String, Class<?>> paramsIn){
        this.dnc_params = paramsIn;
        return (PropertyType) this;
    }
    public LinkedHashMap<String, Class<?>> getDNCParameters(){
        return dnc_params;
    }

    public PropertyType setDNCMethodBody(String body){
        this.dnc_methodBody = body;
        return (PropertyType) this;
    }
    public String getDNCMethodBody(){
        return dnc_methodBody;
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
