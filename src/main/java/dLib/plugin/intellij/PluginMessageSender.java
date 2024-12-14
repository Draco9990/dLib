package dLib.plugin.intellij;

import java.util.LinkedHashMap;

public class PluginMessageSender {
    public static void send_addMethodToClass(String referenceClass, String returnType, String methodName, LinkedHashMap<String, String> methodParameters, String methodBody){
        PluginManager.sendMessage("addMethod", referenceClass, returnType, methodName, methodParameters, methodBody);
    }

    public static void send_renameMethodInClass(String referenceClass, String oldMethodName, String newMethodName, LinkedHashMap<String, String> methodParameters){
        PluginManager.sendMessage("renameMethod", referenceClass, oldMethodName, newMethodName, methodParameters);
    }

    public static void send_removeMethodFromClass(String referenceClass, String methodName, LinkedHashMap<String, String> methodParameters){
        PluginManager.sendMessage("removeMethod", referenceClass, methodName, methodParameters);
    }

    public static void send_addVariableToClass(String referenceClass, Class<?> variableType, String variableName){
        send_addVariableToClass(referenceClass, variableType.getName(), variableName);
    }
    public static void send_addVariableToClass(String referenceClass, String variableType, String variableName){
        PluginManager.sendMessage("addVariable", referenceClass, variableType, variableName);
    }

    public static void send_renameVariableInClass(String referenceClass, String oldName, String newName){
        PluginManager.sendMessage("renameVariable", referenceClass, oldName, newName);
    }

    public static void send_removeVariableFromClass(String referenceClass, String variableName){
        PluginManager.sendMessage("removeVariable", referenceClass, variableName);
    }
}
