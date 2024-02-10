package dLib.tools.screeneditor.screens.toolbar;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.util.settings.Setting;

public class ScreenEditorPropertiesScreen extends AbstractScreenEditorToolbarScreen {
    /** Constructor */
    public ScreenEditorPropertiesScreen(){
        super();

        hide();
    }

    public void createPropertiesFor(ScreenEditorItem item){
        clearScreen();

        int elementHeight = 50;
        int elementSpacing = 10;
        int yPos = 1060 - elementHeight - elementSpacing;
        for(Setting<?> setting : item.getPropertiesForItem()){
            addInteractableElement(setting.makeUIFor(1508, yPos, 404, elementHeight));
            yPos -= (elementHeight + elementSpacing);
        }
    }

    public void clearScreen(){
        interactableElements.getElements().clear();
    }
}
