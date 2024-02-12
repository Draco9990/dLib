package dLib.tools.screeneditor.screens.preview;

import com.badlogic.gdx.graphics.Color;
import dLib.DLib;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.themes.UIThemeManager;

import java.util.ArrayList;

public class ScreenEditorPreviewScreen extends AbstractScreen {
    /** Variables */
    private ArrayList<ScreenEditorItem> previewItems = new ArrayList<>();

    /** Constructors */
    public ScreenEditorPreviewScreen(){
        addElement(new Interactable(UIThemeManager.getDefaultTheme().background, 10, 10, 1490, 840){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                ScreenEditorBaseScreen.instance.getActiveItemsManager().clearActiveItems();
            }
        }.setHoveredColor(Color.WHITE));
    }

    /** Preview Item Management */
    public void makeNewPreviewItem(ScreenEditorItem template){
        ScreenEditorItem copy = template.makeCopy();
        copy.postInitialize();
        copy.setBoundsX(10, 1490);
        copy.setBoundsY(10, 840);

        addElement(copy);
        previewItems.add(copy);

        ScreenEditorBaseScreen.instance.getActiveItemsManager().addActiveItem(copy);
        ScreenEditorBaseScreen.instance.getGeneratedData().addElement(copy.getId(), copy.getElementData());
    }

    public void deletePreviewItem(ScreenEditorItem itemToDelete){
        removeElement(itemToDelete);
        previewItems.remove(itemToDelete);

        ScreenEditorBaseScreen.instance.getGeneratedData().removeElement(itemToDelete.getId());
    }

    public ArrayList<ScreenEditorItem> getPreviewItems(){
        return previewItems;
    }

    @Override
    public String getModId() {
        return DLib.getModID();
    }
}