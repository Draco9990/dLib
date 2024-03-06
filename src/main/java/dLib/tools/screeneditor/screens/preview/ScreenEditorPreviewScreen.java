package dLib.tools.screeneditor.screens.preview;

import com.badlogic.gdx.graphics.Color;
import dLib.DLib;
import dLib.plugin.intellij.PluginManager;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.BackgroundScreenEditorItem;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.themes.UIThemeManager;
import dLib.util.Reflection;
import dLib.util.TextureManager;

import java.util.ArrayList;

public class ScreenEditorPreviewScreen extends AbstractScreen {
    /** Variables */
    private ArrayList<ScreenEditorItem> previewItems = new ArrayList<>();

    public static int xOffset = 10;
    public static int yOffset = 10;

    public static int width = 1490;
    public static int height = 840;

    /** Constructors */
    public ScreenEditorPreviewScreen(){
        addElement(new Interactable(TextureManager.getTexture("dLibResources/images/ui/Transparent.png"), xOffset, yOffset, width, height){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                ScreenEditorBaseScreen.instance.getActiveItemsManager().clearActiveItems();
            }
        }.setHoveredColor(Color.WHITE));
    }

    /** Preview Item Management */
    public void addPreviewItem(ScreenEditorItem item){
        addElement(item);
        previewItems.add(item);

        ScreenEditorBaseScreen.instance.getActiveItemsManager().addActiveItem(item);
    }
    public ScreenEditorItem makeNewPreviewItem(Class<? extends ScreenEditorItem> template){
        ScreenEditorItem copy = (ScreenEditorItem) Reflection.invokeMethod("makeNewInstance", template);
        if(copy == null){
            return null;
        }

        copy.postInitialize();
        copy.setBoundsX(ScreenEditorPreviewScreen.xOffset, ScreenEditorPreviewScreen.xOffset + ScreenEditorPreviewScreen.width);
        copy.setBoundsY(ScreenEditorPreviewScreen.yOffset, ScreenEditorPreviewScreen.yOffset + ScreenEditorPreviewScreen.height);

        PluginManager.sendMessage("screenElementAdd", ScreenEditorBaseScreen.instance.getEditingScreen(), copy.getId(), copy.getLiveInstanceType().getName());

        addPreviewItem(copy);

        return copy;
    }

    public void deletePreviewItem(ScreenEditorItem itemToDelete){
        removeElement(itemToDelete);
        previewItems.remove(itemToDelete);
    }

    public ArrayList<ScreenEditorItem> getPreviewItems(){
        return previewItems;
    }

    @Override
    public String getModId() {
        return DLib.getModID();
    }
}