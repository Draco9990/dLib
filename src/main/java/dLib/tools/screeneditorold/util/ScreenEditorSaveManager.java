package dLib.tools.screeneditorold.util;

import dLib.code.external.ExternalEditorCommunicationManager;
import dLib.tools.screeneditorold.screensold.ScreenEditorBaseScreen;
import dLib.tools.screeneditorold.ui.items.editoritems.ScreenEditorItem;
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

        ExternalEditorCommunicationManager.sendMessage("saveScreen", scrName[scrName.length - 1], SerializationHelpers.toString(screenData));
    }

    //endregion
}
