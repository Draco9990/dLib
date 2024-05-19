package dLib.tools.screeneditor.util;

import dLib.plugin.intellij.PluginManager;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.editoritems.ScreenEditorItem;
import dLib.ui.screens.GeneratedAbstractScreen;
import dLib.util.SerializationHelpers;

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
        GeneratedAbstractScreen.GeneratedScreenData screenData = new GeneratedAbstractScreen.GeneratedScreenData();
        screenData.screenClass = screenEditor.getEditingScreen();
        for(ScreenEditorItem previewItem : screenEditor.getPreviewScreen().getPreviewItems()){
            screenData.data.add(previewItem.getElementData());
        }

        String[] scrName = screenData.screenClass.split("\\.");

        PluginManager.sendMessage("saveScreen", scrName[scrName.length - 1], SerializationHelpers.toString(screenData));
    }

    //endregion
}
