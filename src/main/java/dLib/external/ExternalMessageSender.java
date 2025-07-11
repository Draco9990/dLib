package dLib.external;

import dLib.tools.uicreator.ui.elements.RootElement;
import dLib.util.SerializationHelpers;

import java.util.LinkedHashMap;

public class ExternalMessageSender {
    public static void send_createNewUIElement(String elementName){
        ExternalEditorCommunicationManager.sendMessage("createNewUIElement", elementName);
    }
    public static void send_saveUIElement(String className, RootElement.RootElementData data){
        ExternalEditorCommunicationManager.sendMessage("saveUIElement", className, SerializationHelpers.toString(data));
    }

    public static void send_addMethodToClass(String referenceClass, String returnType, String methodName, LinkedHashMap<String, String> methodParameters, String methodBody){
        ExternalEditorCommunicationManager.sendMessage("addMethod", referenceClass, returnType, methodName, methodParameters, methodBody);
    }

    public static void send_renameMethodInClass(String referenceClass, String oldMethodName, String newMethodName, LinkedHashMap<String, String> methodParameters){
        ExternalEditorCommunicationManager.sendMessage("renameMethod", referenceClass, oldMethodName, newMethodName, methodParameters);
    }

    public static void send_removeMethodFromClass(String referenceClass, String methodName, LinkedHashMap<String, String> methodParameters){
        ExternalEditorCommunicationManager.sendMessage("removeMethod", referenceClass, methodName, methodParameters);
    }

    public static void send_addVariableToClass(String referenceClass, Class<?> variableType, String variableName){
        send_addVariableToClass(referenceClass, variableType.getName(), variableName);
    }
    public static void send_addVariableToClass(String referenceClass, String variableType, String variableName){
        ExternalEditorCommunicationManager.sendMessage("addVariable", referenceClass, variableType, variableName);
    }

    public static void send_renameVariableInClass(String referenceClass, String oldName, String newName){
        ExternalEditorCommunicationManager.sendMessage("renameVariable", referenceClass, oldName, newName);
    }

    public static void send_removeVariableFromClass(String referenceClass, String variableName){
        ExternalEditorCommunicationManager.sendMessage("removeVariable", referenceClass, variableName);
    }
}
