package dLib.tools.screeneditor.ui.items.implementations.preview;

import dLib.plugin.intellij.PluginManager;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.Reflection;
import dLib.util.TextureManager;

import java.util.ArrayList;

public class ScreenEditorPreview extends UIElement {
    //region Variables

    public static int xOffset = 10; //TODO RF remove
    public static int yOffset = 10;

    public static int width = 1490;
    public static int height = 840;

    //endregion

    //region Constructors

    public ScreenEditorPreview(){
        super(10, 10, 1490, 840);

        addChildNCS(new Renderable(TextureManager.getTexture("dLibResources/images/ui/Transparent.png"), 0, 0, getWidth(), getHeight()));
    }

    //endregion

    //region Methods

    //region Preview Item Management

    public void addPreviewItem(ScreenEditorItem item){
        addChildNCS(item);

        getParent().getActiveItemsManager().addActiveItem(item);
    }
    public ScreenEditorItem makeNewPreviewItem(Class<? extends ScreenEditorItem> template){
        ScreenEditorItem copy = (ScreenEditorItem) Reflection.invokeMethod("makeNewInstance", template);
        if(copy == null){
            return null;
        }

        copy.screenEditor = getParent();

        copy.postInitialize();

        String idPrefix = copy.getClass().getSimpleName().replace("ScreenEditorItem", "") + "_";
        int i = 1;
        while(findChildById(idPrefix + i) != null){
            i++;
        }
        copy.setID(idPrefix + i);

        copy.setBoundWithinParent(true);

        PluginManager.sendMessage("screenElementAdd", getParent().getEditingScreen(), copy.getId(), copy.getLiveInstanceType().getName());

        addPreviewItem(copy);

        return copy;
    }
    public void deletePreviewItem(ScreenEditorItem itemToDelete){
        removeChild(itemToDelete);

        PluginManager.sendMessage("screenElementRemove", getParent().getEditingScreen(), itemToDelete.getId());
    }

    //endregion

    @Override
    public ScreenEditorBaseScreen getParent() {
        return (ScreenEditorBaseScreen) super.getParent();
    }

    public ArrayList<ScreenEditorItem> getPreviewItems(){
        ArrayList<ScreenEditorItem> previewItems = new ArrayList<>();
        for(UIElementChild child : children){
            if(child.element instanceof ScreenEditorItem){
                previewItems.add((ScreenEditorItem) child.element);
            }
        }

        return previewItems;
    }

    //endregion
}