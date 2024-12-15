package dLib.code.external;

import dLib.tools.screeneditorold.screensold.ScreenEditorBaseScreen;

import java.util.List;

public class MessageAnalyzer {
    public static void analyzeMessage(NetworkMessage message){
        String request = message.getRequest();

        if(request.equals("openScreenEditor")){
            String screenClass = message.getData(String.class);

            ScreenEditorBaseScreen screen = new ScreenEditorBaseScreen(screenClass);
            screen.open();
        }

        if(request.equals("screenListCallback")){
            List<String> screens = message.getData(List.class);
            /*if(ScreenManager.getCurrentScreen() instanceof ScreenEditorScreenPickerScreen){
                ((ScreenEditorScreenPickerScreen) ScreenManager.getCurrentScreen()).setScreenOptions(screens);
            }*/
        }
    }
}
