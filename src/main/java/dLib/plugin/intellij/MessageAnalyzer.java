package dLib.plugin.intellij;

import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.ui.screens.ScreenManager;

public class MessageAnalyzer {
    public static void analyzeMessage(NetworkMessage message){
        String request = message.getRequest();

        if(request.equals("openScreenEditor")){
            String screenClass = message.getData(String.class);

            ScreenManager.openScreen(new ScreenEditorBaseScreen(screenClass));
        }
    }
}
