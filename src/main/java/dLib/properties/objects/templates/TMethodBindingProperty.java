package dLib.properties.objects.templates;

import basemod.Pair;
import dLib.code.LinkedEditor;
import dLib.code.LinkedEditorManager;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.staticbindings.NoneMethodBinding;

import java.io.Serializable;
import java.util.*;

public abstract class TMethodBindingProperty<PropertyType> extends TProperty<MethodBinding, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private Class<?> dynamic_returnType = void.class;
    private String dynamic_defaultMethodName = "MethodBinding_" + UUID.randomUUID().toString().replace("-", "");
    private LinkedHashMap<String, Class<?>> dynamic_params = new LinkedHashMap<>();
    private String dynamic_methodBody = "{\n//IMPLEMENTATION HERE\n}";

    private boolean dynamicBindingOnly = false;
    private boolean noDynamicBinding = false;

    private boolean dynamicCreated = false;

    //endregion

    //region Constructors

    public TMethodBindingProperty(MethodBinding value) {
        super(value);
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

    @Override
    public void onValueChanged(MethodBinding oldValue, MethodBinding newValue) {
        super.onValueChanged(oldValue, newValue);

        if(newValue instanceof DynamicMethodBinding){
            dynamicCreated = false;
            ((DynamicMethodBinding) newValue).addOnBoundMethodChangedConsumer((oldVal, newVal) -> {
                if(dynamicCreated){
                    LinkedEditor editor = LinkedEditorManager.get().getActiveEditor();
                    if(editor == null){
                        return;
                    }

                    editor.renameMethodInClass(oldVal, newVal, getDynamicCreationParametersAsStringMap());
                }
            });
        }
        else if(oldValue instanceof DynamicMethodBinding && !((DynamicMethodBinding) oldValue).getBoundMethod().isEmpty()){
            LinkedEditor editor = LinkedEditorManager.get().getActiveEditor();
            if(editor == null){
                return;
            }

            editor.removeMethodFromClass(((DynamicMethodBinding) oldValue).getBoundMethod(), getDynamicCreationParametersAsStringMap());
        }
    }

    //endregion

    //region Dynamic Method Creation

    public PropertyType setDynamicCreationReturnType(Class<?> returnType){
        this.dynamic_returnType = returnType;
        return (PropertyType) this;
    }
    public Class<?> getDynamicCreationReturnType(){
        return dynamic_returnType;
    }

    public PropertyType setDynamicCreationDefaultMethodName(String methodName){
        this.dynamic_defaultMethodName = methodName;
        return (PropertyType) this;
    }
    public String getDynamicCreationDefaultMethodName(){
        return dynamic_defaultMethodName;
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

    public void createDynamicMethod(){
        if(dynamicCreated || !(getValue() instanceof DynamicMethodBinding)){
            return;
        }

        LinkedEditor editor = LinkedEditorManager.get().getActiveEditor();
        if(editor == null){
            return;
        }

        editor.addMethodToClass(getDynamicCreationReturnType().getName(), ((DynamicMethodBinding) getValue()).getBoundMethod(), getDynamicCreationParametersAsStringMap(), getDynamicCreationMethodBody());
    }

    public LinkedHashMap<String, String> getDynamicCreationParametersAsStringMap(){
        LinkedHashMap<String, String> convertedParameters = new LinkedHashMap<>();
        for(Map.Entry<String, Class<?>> param : getDynamicCreationParameters().entrySet()){
            convertedParameters.put(param.getKey(), param.getValue().getName());
        }
        return convertedParameters;
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

    //endregion
}
