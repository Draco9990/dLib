package dLib.tools.screeneditor;

import com.badlogic.gdx.graphics.Color;
import dLib.DLib;
import dLib.tools.screeneditor.ui.items.preview.UIPreviewItem;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.themes.UIThemeManager;

public class ScreenEditorPreviewScreen extends AbstractScreen {
    /** Variables */

    /** Constructors */
    public ScreenEditorPreviewScreen(){
        addElementToBackground(new Interactable(UIThemeManager.getDefaultTheme().background, 10, 10, 1490, 840){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                ScreenEditorBaseScreen.instance.getActiveItemsManager().clearActiveItems();
            }
        }.setHoveredColor(Color.WHITE));
    }

    /** Class methods */
    public void makeNewPreviewItem(UIPreviewItem template){
        UIPreviewItem copy = template.makeCopy();
        copy.setBoundsX(10, 1490);
        copy.setBoundsY(10, 840);

        addInteractableElement(copy);

        ScreenEditorBaseScreen.instance.getActiveItemsManager().addActiveItem(copy);
    }

    public void deletePreviewItem(UIPreviewItem itemToDelete){
        removeInteractableElement(itemToDelete);
    }

    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
