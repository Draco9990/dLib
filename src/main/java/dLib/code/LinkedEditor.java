package dLib.code;

import dLib.plugin.intellij.PluginManager;

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
        PluginManager.sendMessage("addMethod", getWorkingClass(), returnType, methodName, methodParameters, methodBody);
    }
    public void renameMethodInClass(String oldMethodName, String newMethodName, LinkedHashMap<String, String> methodParameters){
        PluginManager.sendMessage("renameMethod", getWorkingClass(), oldMethodName, newMethodName, methodParameters);
    }
    public void removeMethodFromClass(String methodName, LinkedHashMap<String, String> methodParameters){
        PluginManager.sendMessage("removeMethod", getWorkingClass(), methodName, methodParameters);
    }

    public void addVariableToClass(Class<?> variableType, String variableName){
        addVariableToClass(variableType.getName(), variableName);
    }
    public void addVariableToClass(String variableType, String variableName){
        PluginManager.sendMessage("addVariable", getWorkingClass(), variableType, variableName);
    }
    public void renameVariableInClass(String oldName, String newName){
        PluginManager.sendMessage("renameVariable", getWorkingClass(), oldName, newName);
    }
    public void removeVariableFromClass(String variableName){
        PluginManager.sendMessage("removeVariable", getWorkingClass(), variableName);
    }

    //endregion

    //endregion
}
