package dLib.tools.screeneditor.screens.toolbar;

import com.badlogic.gdx.graphics.Color;
import dLib.DLib;
import dLib.tools.screeneditor.ui.items.preview.UIPreviewItem;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.themes.UITheme;
import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.StringSetting;

public class ScreenEditorPropertiesScreen extends AbstractScreenEditorToolbarScreen {
    /** Constructor */
    public ScreenEditorPropertiesScreen(){
        super();

        hide();
    }

    public void createPropertiesFor(UIPreviewItem item){
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
