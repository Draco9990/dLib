package dLib.tools.screeneditor.screens.toolbar;

import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.ListBox;

import java.util.ArrayList;

public class ScreenEditorElementListScreen extends AbstractScreenEditorToolbarScreen {
    private ListBox<ScreenEditorItem> previewItemList;

    /** Constructors */
    public ScreenEditorElementListScreen(){
        super();

        previewItemList = new ListBox<ScreenEditorItem>(1508, 10, 404, 1060){
            @Override
            public void onItemSelectionChanged(ArrayList<ScreenEditorItem> items) {
                super.onItemSelectionChanged(items);

                if(items.isEmpty()) return;
                ScreenEditorBaseScreen.instance.getActiveItemsManager().addActiveItem(items.get(0));
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

    @Override
    public void show() {
        super.show();

        refreshItemList();
    }

    public void refreshItemList(){
        previewItemList.clearItems();
        for(ScreenEditorItem item : ScreenEditorBaseScreen.instance.getPreviewScreen().getPreviewItems()){
            previewItemList.addItem(item);
        }
    }
}
