package dLib.tools.screeneditor.util;

import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.AbstractScreenData;
import dLib.ui.screens.AbstractScreen;

public class ScreenEditorSaveManager {
    /** Save */
    public void save(){
        AbstractScreenData screenData = new AbstractScreenData();
        for(ScreenEditorItem item : ScreenEditorBaseScreen.instance.getPreviewScreen().getPreviewItems()){
            screenData.data.add(item.getElementData());
        }

        screenData.serialize("D:/saveData.dscreen");
    }

    public static ScreenEditorBaseScreen load(){
        return new ScreenEditorBaseScreen(AbstractScreenData.deserialize("D:/savedata.dscreen"));
    }
}
