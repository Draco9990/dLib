package dLib.plugin.intellij;

import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;

import java.util.LinkedHashMap;

public class PluginMessageSender {
    public static void Send_AddMethodToClass(String referenceClass, String returnType, String methodName, LinkedHashMap<String, String> methodParameters, String methodBody){
        PluginManager.sendMessage("addMethod", referenceClass, returnType, methodName, methodParameters, methodBody);
    }

    public static void Send_RenameMethodInClass(String referenceClass, String oldMethodName, String newMethodName, LinkedHashMap<String, String> methodParameters){
        PluginManager.sendMessage("renameMethod", referenceClass, oldMethodName, newMethodName, methodParameters);
    }

    public static void Send_RemoveMethodInClass(String referenceClass, String methodName, LinkedHashMap<String, String> methodParameters){
        PluginManager.sendMessage("removeMethod", referenceClass, methodName, methodParameters);
    }
}
