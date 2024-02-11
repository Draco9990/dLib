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

        int elementHeight = 100;
        int elementSpacing = 5;
        int yPos = 1080 - elementHeight - elementSpacing;
        for(Setting<?> setting : item.getPropertiesForItem()){
            addInteractableElement(setting.makeUIFor(1508, yPos, 404, elementHeight));
            yPos -= (elementHeight + elementSpacing);
        }
    }

    public void clearScreen(){
        interactableElements.getElements().clear();
    }
}
