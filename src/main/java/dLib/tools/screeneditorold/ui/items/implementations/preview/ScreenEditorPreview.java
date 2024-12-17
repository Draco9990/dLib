package dLib.tools.screeneditorold.ui.items.implementations.preview;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.code.external.ExternalEditorMessageSender;
import dLib.tools.screeneditorold.screensold.ScreenEditorBaseScreen;
import dLib.tools.screeneditorold.ui.items.editoritems.ScreenEditorItem;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.DLibLogger;
import dLib.util.TextureManager;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.bounds.Bound;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;

public class ScreenEditorPreview extends UIElement {
    //region Variables

    public static int width = 1490;
    public static int height = 840;

    private Renderable grid;

    //endregion

    //region Constructors

    public ScreenEditorPreview(){
        super(Pos.px(10), Pos.px(10), Dim.fill(), Dim.fill());

        addChildNCS(new Renderable(Tex.stat(TextureManager.getTexture("dLibResources/images/ui/Transparent.png")), Pos.px(0), Pos.px(0), getWidthRaw(), getHeightRaw()));

        grid = new Renderable(Tex.stat(TextureManager.getTexture("dLibResources/images/ui/screeneditor/ScreenEditorGrid.png")), Pos.px(0), Pos.px(0), getWidthRaw(), getHeightRaw());
    }

    //endregion

    //region Methods

    @Override
    protected void renderChildren(SpriteBatch sb) {
        super.renderChildren(sb);
        if(getParent().getEditorProperties().isGridOn()){
            grid.render(sb);
        }
    }

    //region Preview Item Management

    public void addPreviewItem(ScreenEditorItem item){
        addChildNCS(item);

        getParent().getActiveItemsManager().addActiveItem(item);
    }
    public ScreenEditorItem<?, ?> makeNewPreviewItem(Class<? extends ScreenEditorItem> template){
        try{
            ScreenEditorItem<?, ?> copy = template.newInstance();

            validatePreviewItem(copy);
            ExternalEditorMessageSender.send_addVariableToClass(getParent().getEditingScreen(), copy.getElementClass(), copy.getId());
            addPreviewItem(copy);
            return copy;
        }catch (Exception e){
            DLibLogger.logError("Failed to create new instance of a screen editor item due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }
    public ScreenEditorItem<?,?> duplicatePreviewItem(ScreenEditorItem template){
        ScreenEditorItem copy = template.copy();
        if(copy == null){
            return null;
        }

        validatePreviewItem(copy);
        ExternalEditorMessageSender.send_addVariableToClass(getParent().getEditingScreen(), copy.getElementClass(), copy.getId());

        addPreviewItem(copy);

        return copy;
    }
    public void validatePreviewItem(ScreenEditorItem item){
        item.setScreenEditor(getParent());

        String idPrefix = item.getClass().getSimpleName().replace("ScreenEditorItem", "") + "_";
        int i = 1;
        while(findChildById(idPrefix + i) != null){
            i++;
        }
        item.setID(idPrefix + i);

        item.setContainerBounds(Bound.parent(this));
    }
    public void deletePreviewItem(ScreenEditorItem itemToDelete){
        itemToDelete.dispose();

        ExternalEditorMessageSender.send_removeVariableFromClass(getParent().getEditingScreen(), itemToDelete.getId());
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