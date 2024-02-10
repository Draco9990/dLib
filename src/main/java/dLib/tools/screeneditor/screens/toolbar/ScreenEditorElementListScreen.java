package dLib.tools.screeneditor.screens.toolbar;

import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.ListBox;

public class ScreenEditorElementListScreen extends AbstractScreenEditorToolbarScreen {
    private ListBox<ScreenEditorItem> previewItemList;

    /** Constructors */
    public ScreenEditorElementListScreen(){
        super();

        previewItemList = new ListBox<ScreenEditorItem>(1508, 10, 404, 1060){
            @Override
            public void onItemSelected(ScreenEditorItem item) {
                super.onItemSelected(item);
                ScreenEditorBaseScreen.instance.getActiveItemsManager().addActiveItem(item);
            }

            @Override
            public void postMakeRenderElementForItem(ScreenEditorItem item, CompositeUIElement compositeUIElement) {
                super.postMakeRenderElementForItem(item, compositeUIElement);

                Button button = (Button) compositeUIElement.middle;
                button.setOnHoveredConsumer(() -> item.setHighlight(true));
                button.setOnUnhoveredConsumer(() -> item.setHighlight(false));
            }
        }.setTitle("Scene Elements:");
        previewItemList.getBackground().setImage(null);
        addInteractableElement(previewItemList);

        hide();
    }

    @Override
    public void show() {
        super.show();

        previewItemList.clearItems();
        for(ScreenEditorItem item : ScreenEditorBaseScreen.instance.getPreviewScreen().getPreviewItems()){
            previewItemList.addItem(item);
        }
    }
}
