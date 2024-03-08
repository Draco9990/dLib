package dLib.tools.screeneditor.ui.items.implementations.toolbar;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.ListBox;

import java.util.ArrayList;

public class ScreenEditorElementList extends AbstractScreenEditorToolbar {
    //region Variables

    private ListBox<ScreenEditorItem> previewItemList;

    //endregion

    //region Constructors

    public ScreenEditorElementList(){
        super();

        ScreenEditorElementList self = this;

        previewItemList = new ListBox<ScreenEditorItem>(0, 0, getWidth(), getHeight()){
            @Override
            public void onItemSelectionChanged(ArrayList<ScreenEditorItem> items) {
                super.onItemSelectionChanged(items);

                if(items.isEmpty()) return;
                self.getParent().getActiveItemsManager().addActiveItem(items.get(0));
            }

            @Override
            public void postMakeCompositeForItem(ScreenEditorItem item, CompositeUIElement compositeUIElement) {
                super.postMakeCompositeForItem(item, compositeUIElement);

                Button button = (Button) compositeUIElement.middle;
                button.addOnHoveredConsumer(() -> item.setHighlight(true));
                button.addOnUnhoveredConsumer(() -> item.setHighlight(false));
            }
        }.setTitle("Scene Elements:").setInvertedItemOrder(true);
        previewItemList.getBackground().setImage(null);
        addChildNCS(previewItemList);

        hide();
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
