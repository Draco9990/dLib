package dLib.code;

import dLib.code.external.ExternalEditorMessageSender;

import java.util.LinkedHashMap;

public class LinkedEditor {

    private String workingClass = null;

    //region Methods

    //region Working Class

    public void setWorkingClass(String workingClass){
        this.workingClass = workingClass;
    }

    public String getWorkingClass(){
        return workingClass;
    }

    //endregion

    //region Callbacks

    public void addMethodToClass(String returnType, String methodName, LinkedHashMap<String, String> methodParameters, String methodBody){
        ExternalEditorMessageSender.send_addMethodToClass(getWorkingClass(), returnType, methodName, methodParameters, methodBody);
    }
    public void renameMethodInClass(String oldMethodName, String newMethodName, LinkedHashMap<String, String> methodParameters){
        ExternalEditorMessageSender.send_renameMethodInClass(getWorkingClass(), oldMethodName, newMethodName, methodParameters);
    }
    public void removeMethodFromClass(String methodName, LinkedHashMap<String, String> methodParameters){
        ExternalEditorMessageSender.send_removeMethodFromClass(getWorkingClass(), methodName, methodParameters);
    }

    public void addVariableToClass(Class<?> variableType, String variableName){
        addVariableToClass(variableType.getName(), variableName);
    }
    public void addVariableToClass(String variableType, String variableName){
        ExternalEditorMessageSender.send_addVariableToClass(getWorkingClass(), variableType, variableName);
    }
    public void renameVariableInClass(String oldName, String newName){
        ExternalEditorMessageSender.send_renameVariableInClass(getWorkingClass(), oldName, newName);
    }
    public void removeVariableFromClass(String variableName){
        ExternalEditorMessageSender.send_removeVariableFromClass(getWorkingClass(), variableName);
    }

    //endregion

    //endregion
}
