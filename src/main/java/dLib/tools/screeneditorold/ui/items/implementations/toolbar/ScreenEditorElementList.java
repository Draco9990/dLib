package dLib.tools.screeneditorold.ui.items.implementations.toolbar;

import dLib.tools.screeneditorold.ui.items.editoritems.ScreenEditorItem;
import dLib.tools.screeneditorold.ui.items.implementations.preview.ScreenEditorPreview;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Button;
import dLib.ui.elements.items.itembox.VerticalListBox;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
import java.util.Collections;

public class ScreenEditorElementList extends AbstractScreenEditorToolbar {
    //region Variables

    private VerticalListBox<ScreenEditorItem> previewItemList;

    //endregion

    //region Constructors

    public ScreenEditorElementList(){
        super();

        ScreenEditorElementList self = this;

        previewItemList = new VerticalListBox<ScreenEditorItem>(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()){
            @Override
            public void onItemSelectionChanged(ArrayList<ScreenEditorItem> items) {
                super.onItemSelectionChanged(items);

                if(items.isEmpty()) return;
                self.getParent().getActiveItemsManager().addActiveItem(items.get(0));
            }

            @Override
            public void postMakeWrapperForItem(ScreenEditorItem item, UIElement itemUI) {
                super.postMakeWrapperForItem(item, itemUI);

                Button button = (Button) itemUI.findChildById("MainSection");
                button.onHoveredEvent.subscribeManaged(() -> item.setHighlight(true));
                button.onUnhoveredEvent.subscribeManaged(() -> item.setHighlight(false));
            }
        };
        previewItemList.setInvertedItemOrder(true);
        previewItemList.setCanReorder(true);
        previewItemList.onItemsSwappedEvent.subscribeManaged((screenEditorItem, screenEditorItem2) -> {
            ScreenEditorPreview baseScreen = getParent().getPreviewScreen();
            int index1 = baseScreen.getPreviewItems().indexOf(screenEditorItem);
            int index2 = baseScreen.getPreviewItems().indexOf(screenEditorItem2);
            Collections.swap(baseScreen.getPreviewItems(), index1, index2);

            int childIndex1 = baseScreen.getChildren().indexOf(screenEditorItem);
            int childIndex2 = baseScreen.getChildren().indexOf(screenEditorItem2);
            baseScreen.swapChildren(childIndex1, childIndex2);
        });
        previewItemList.setImage(new TextureNoneBinding());
        addChild(previewItemList);

        hideAndDisable();
    }

    //endregion

    //region Methods

    @Override
    public void setVisibility(boolean isVisible) {
        super.setVisibility(isVisible);

        if(isVisible) refreshItemList();
    }

    public void refreshItemList(){
        previewItemList.clearItems();
        for(ScreenEditorItem item : getParent().getPreviewScreen().getPreviewItems()){
            previewItemList.addItem(item);
        }
    }

    //endregion
}
