package dLib.tools.screeneditor.ui.items.implementations.toolbar;

import dLib.tools.screeneditor.ui.items.editoritems.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.implementations.preview.ScreenEditorPreview;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.HorizontalListBox;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.elements.prefabs.VerticalListBox;

import java.util.ArrayList;
import java.util.Collections;

public class ScreenEditorElementList extends AbstractScreenEditorToolbar {
    //region Variables

    private ListBox<ScreenEditorItem> previewItemList;

    //endregion

    //region Constructors

    public ScreenEditorElementList(){
        super();

        ScreenEditorElementList self = this;

        previewItemList = new HorizontalListBox<ScreenEditorItem>(0, 0, getWidth(), getHeight()){
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
                button.addOnHoveredConsumer(() -> item.setHighlight(true));
                button.addOnUnhoveredConsumer(() -> item.setHighlight(false));
            }
        }.setTitle("Scene Elements:").setInvertedItemOrder(true).setCanReorder(true).addOnElementsSwappedListener((screenEditorItem, screenEditorItem2) -> {
            ScreenEditorPreview baseScreen = getParent().getPreviewScreen();
            int index1 = baseScreen.getPreviewItems().indexOf(screenEditorItem);
            int index2 = baseScreen.getPreviewItems().indexOf(screenEditorItem2);
            Collections.swap(baseScreen.getPreviewItems(), index1, index2);

            int childIndex1 = baseScreen.getChildren().indexOf(screenEditorItem);
            int childIndex2 = baseScreen.getChildren().indexOf(screenEditorItem2);
            baseScreen.swapChildren(childIndex1, childIndex2);
        });
        previewItemList.getBackground().setImage(null);
        addChildNCS(previewItemList);

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
