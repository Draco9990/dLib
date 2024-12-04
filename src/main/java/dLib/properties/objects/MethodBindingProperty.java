package dLib.properties.objects;

import dLib.properties.ui.elements.MethodBindingPropertyEditor;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.MethodBindingHelpers;
import dLib.util.bindings.method.NoneMethodBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

public class MethodBindingProperty extends CustomProperty<MethodBinding> implements Serializable {
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

    public MethodBindingProperty(MethodBinding value) {
        super(value);

        propertyEditorClass = MethodBindingPropertyEditor.class;
    }

    public MethodBindingProperty() {
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

    public MethodBindingProperty setDNCReturnType(Class<?> returnType){
        this.dnc_returnType = returnType;
        return this;
    }
    public Class<?> getDNCReturnType(){
        return dnc_returnType;
    }

    public MethodBindingProperty setDNCMethodName(String methodName){
        this.dnc_methodName = methodName;
        return this;
    }
    public String getDNCMethodName(){
        return dnc_methodName;
    }

    public MethodBindingProperty addDNCParameter(String paramName, Class<?> paramType){
        dnc_params.put(paramName, paramType);
        return this;
    }
    public MethodBindingProperty setDNCParameters(LinkedHashMap<String, Class<?>> paramsIn){
        this.dnc_params = paramsIn;
        return this;
    }
    public LinkedHashMap<String, Class<?>> getDNCParameters(){
        return dnc_params;
    }

    public MethodBindingProperty setDNCMethodBody(String body){
        this.dnc_methodBody = body;
        return this;
    }
    public String getDNCMethodBody(){
        return dnc_methodBody;
    }

    //endregion

    //region Dynamic Method Binding

    public MethodBindingProperty setDynamicBindingsOnly(){
        dynamicBindingOnly = true;
        noDynamicBinding = false;
        if(!(getValue() instanceof DynamicMethodBinding)){
            setValue(new DynamicMethodBinding(""));
        }
        return this;
    }

    public MethodBindingProperty disableDynamicBindings(){
        dynamicBindingOnly = false;
        noDynamicBinding = true;
        if(getValue() instanceof DynamicMethodBinding){
            setValue(new NoneMethodBinding());
        }
        return this;
    }

    //endregion

    @Override
    public MethodBindingProperty setName(String newTitle) {
        super.setName(newTitle);
        return this;
    }

    @Override
    public ArrayList<MethodBinding> getAllOptions() {
        return MethodBindingHelpers.getPremadeMethodBindings();
    }

    //endregion
}
