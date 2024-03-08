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
        super();
        addChildCS(new Interactable(TextureManager.getTexture("dLibResources/images/ui/Transparent.png"), 0, 0, getWidth(), getHeight()){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                ScreenEditorBaseScreen.instance.getActiveItemsManager().clearActiveItems();
            }
        }.setHoveredColor(Color.WHITE));
    }

    /** Preview Item Management */
    public void addPreviewItem(ScreenEditorItem item){
        addChildNCS(item);
        previewItems.add(item);

        ScreenEditorBaseScreen.instance.getActiveItemsManager().addActiveItem(item);
    }
    public ScreenEditorItem makeNewPreviewItem(Class<? extends ScreenEditorItem> template){
        ScreenEditorItem copy = (ScreenEditorItem) Reflection.invokeMethod("makeNewInstance", template);
        if(copy == null){
            return null;
        }

        copy.postInitialize();

        String idPrefix = copy.getClass().getSimpleName().replace("ScreenEditorItem", "") + "_";
        int i = 1;
        while(findPreviewItemById(idPrefix + i) != null){
            i++;
        }
        copy.setID(idPrefix + i);

        copy.setBoundsX(ScreenEditorPreviewScreen.xOffset, ScreenEditorPreviewScreen.xOffset + ScreenEditorPreviewScreen.width);
        copy.setBoundsY(ScreenEditorPreviewScreen.yOffset, ScreenEditorPreviewScreen.yOffset + ScreenEditorPreviewScreen.height);

        PluginManager.sendMessage("screenElementAdd", ScreenEditorBaseScreen.instance.getEditingScreen(), copy.getId(), copy.getLiveInstanceType().getName());

        addPreviewItem(copy);

        return copy;
    }

    public void deletePreviewItem(ScreenEditorItem itemToDelete){
        removeChild(itemToDelete);
        previewItems.remove(itemToDelete);

        PluginManager.sendMessage("screenElementRemove", ScreenEditorBaseScreen.instance.getEditingScreen(), itemToDelete.getId());
    }

    public ArrayList<ScreenEditorItem> getPreviewItems(){
        return previewItems;
    }

    public ScreenEditorItem findPreviewItemById(String id){
        for(ScreenEditorItem item : previewItems){
            if(item.getId().equals(id)){
                return item;
            }
        }
        return null;
    }

    @Override
    public String getModId() {
        return DLib.getModID();
    }
}