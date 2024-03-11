package dLib.tools.screeneditor.util;

import dLib.plugin.intellij.PluginManager;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;

public class ScreenEditorSaveManager {
    //region Variables

    private ScreenEditorBaseScreen screenEditor;

    //endregion

    //region Constructors

    public ScreenEditorSaveManager(ScreenEditorBaseScreen screenEditor){
        this.screenEditor = screenEditor;
    }

    //endregion

    //region Methods

    public void save(){
        AbstractScreenData screenData = new AbstractScreenData(screenEditor);

        String[] scrName = screenData.screenClass.split("\\.");

        PluginManager.sendMessage("saveScreen", scrName[scrName.length - 1], screenData.serializeToString());
    }

    //endregion
}
