package dLib.external;

import dLib.tools.uicreator.UCEditor;

import java.util.List;

public class ExternalMessageAnalyzer {
    public static void analyzeMessage(NetworkMessage message){
        String request = message.getRequest();

        if(request.equals("onNewUIElementCreated")){
            String screenClass = message.getData(String.class);
            ExternalStatics.workingClass = screenClass;
            UCEditor editor = new UCEditor();
            editor.open();
        }

        if(request.equals("screenListCallback")){
            List<String> screens = message.getData(List.class);
        }
    }
}
