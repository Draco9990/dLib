package dLib.plugin.intellij;

import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.ui.screens.ScreenManager;

import java.util.List;

public class MessageAnalyzer {
    public static void analyzeMessage(NetworkMessage message){
        String request = message.getRequest();

        if(request.equals("openScreenEditor")){
            String screenClass = message.getData(String.class);

            ScreenManager.openScreen(new ScreenEditorBaseScreen(screenClass));
        }

        if(request.equals("screenListCallback")){
            List<String> screens = message.getData(List.class);
            if(ScreenManager.getCurrentScreen() instanceof ScreenEditorScreenPickerScreen){
                ((ScreenEditorScreenPickerScreen) ScreenManager.getCurrentScreen()).setScreenOptions(screens);
            }
        }
    }
}
