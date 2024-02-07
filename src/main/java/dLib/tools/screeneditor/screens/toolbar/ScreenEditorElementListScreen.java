package dLib.tools.screeneditor.screens.toolbar;

import com.badlogic.gdx.graphics.Color;
import dLib.DLib;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.UIPreviewItem;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.themes.UITheme;

public class ScreenEditorElementListScreen extends AbstractScreenEditorToolbarScreen {
    private ListBox<UIPreviewItem> previewItemList;

    /** Constructors */
    public ScreenEditorElementListScreen(){
        super();

        previewItemList = new ListBox<UIPreviewItem>(1508, 10, 404, 1060){
            @Override
            public void onItemSelected(UIPreviewItem item) {
                super.onItemSelected(item);
                ScreenEditorBaseScreen.instance.getActiveItemsManager().addActiveItem(item);
            }

            @Override
            public void postMakeRenderElementForItem(UIPreviewItem item, CompositeUIElement compositeUIElement) {
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
        for(UIPreviewItem item : ScreenEditorBaseScreen.instance.getPreviewScreen().getPreviewItems()){
            previewItemList.addItem(item);
        }
    }
}
