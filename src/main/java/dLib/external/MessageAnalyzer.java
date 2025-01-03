package dLib.external;

import java.util.List;

public class MessageAnalyzer {
    public static void analyzeMessage(NetworkMessage message){
        String request = message.getRequest();

        if(request.equals("onNewUIElementCreated")){
            String screenClass = message.getData(String.class);

            /*ScreenEditorBaseScreen screen = new ScreenEditorBaseScreen(screenClass);
            screen.open();*/
        }

        if(request.equals("screenListCallback")){
            List<String> screens = message.getData(List.class);
            /*if(ScreenManager.getCurrentScreen() instanceof ScreenEditorScreenPickerScreen){
                ((ScreenEditorScreenPickerScreen) ScreenManager.getCurrentScreen()).setScreenOptions(screens);
            }*/
        }
    }
}
