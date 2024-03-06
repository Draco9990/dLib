package dLib.tools.screeneditor.util;

import dLib.plugin.intellij.PluginManager;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.AbstractScreenData;
import dLib.ui.screens.AbstractScreen;

public class ScreenEditorSaveManager {
    /** Save */
    public void save(){
        AbstractScreenData screenData = new AbstractScreenData(ScreenEditorBaseScreen.instance);

        String[] scrName = screenData.screenClass.split("\\.");

        PluginManager.sendMessage("saveScreen", scrName[scrName.length - 1], screenData.serializeToString());
    }
}
