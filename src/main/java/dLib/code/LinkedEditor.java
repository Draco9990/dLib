package dLib.code;

import dLib.code.external.ExternalEditorCommunicationManager;

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
        ExternalEditorCommunicationManager.sendMessage("addMethod", getWorkingClass(), returnType, methodName, methodParameters, methodBody);
    }
    public void renameMethodInClass(String oldMethodName, String newMethodName, LinkedHashMap<String, String> methodParameters){
        ExternalEditorCommunicationManager.sendMessage("renameMethod", getWorkingClass(), oldMethodName, newMethodName, methodParameters);
    }
    public void removeMethodFromClass(String methodName, LinkedHashMap<String, String> methodParameters){
        ExternalEditorCommunicationManager.sendMessage("removeMethod", getWorkingClass(), methodName, methodParameters);
    }

    public void addVariableToClass(Class<?> variableType, String variableName){
        addVariableToClass(variableType.getName(), variableName);
    }
    public void addVariableToClass(String variableType, String variableName){
        ExternalEditorCommunicationManager.sendMessage("addVariable", getWorkingClass(), variableType, variableName);
    }
    public void renameVariableInClass(String oldName, String newName){
        ExternalEditorCommunicationManager.sendMessage("renameVariable", getWorkingClass(), oldName, newName);
    }
    public void removeVariableFromClass(String variableName){
        ExternalEditorCommunicationManager.sendMessage("removeVariable", getWorkingClass(), variableName);
    }

    //endregion

    //endregion
}
