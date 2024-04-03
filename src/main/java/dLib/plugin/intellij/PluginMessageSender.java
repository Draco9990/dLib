package dLib.plugin.intellij;

import java.util.LinkedHashMap;

public class PluginMessageSender {
    public static void Send_AddMethodToClass(String referenceClass, String returnType, String methodName, LinkedHashMap<String, String> methodParameters, String methodBody){
        PluginManager.sendMessage("addMethod", referenceClass, returnType, methodName, methodParameters, methodBody);
    }

    public static void Send_RenameMethodInClass(String referenceClass, String oldMethodName, String newMethodName, LinkedHashMap<String, String> methodParameters){
        PluginManager.sendMessage("renameMethod", referenceClass, oldMethodName, newMethodName, methodParameters);
    }

    public static void Send_RemoveMethodFromClass(String referenceClass, String methodName, LinkedHashMap<String, String> methodParameters){
        PluginManager.sendMessage("removeMethod", referenceClass, methodName, methodParameters);
    }

    public static void Send_AddVariableToClass(String referenceClass, Class<?> variableType, String variableName){
        Send_AddVariableToClass(referenceClass, variableType.getName(), variableName);
    }
    public static void Send_AddVariableToClass(String referenceClass, String variableType, String variableName){
        PluginManager.sendMessage("addVariable", referenceClass, variableType, variableName);
    }

    public static void Send_RenameVariableInClass(String referenceClass, String oldName, String newName){
        PluginManager.sendMessage("renameVariable", referenceClass, oldName, newName);
    }

    public static void Send_RemoveVariableFromClass(String referenceClass, String variableName){
        PluginManager.sendMessage("removeVariable", referenceClass, variableName);
    }
}
