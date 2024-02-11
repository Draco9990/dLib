package dLib.tools.screeneditor.screens.toolbar;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.prefabs.ListBox;
import dLib.util.settings.Setting;

public class ScreenEditorPropertiesScreen extends AbstractScreenEditorToolbarScreen {
    /** Variables */
    private ListBox<Setting<?>> propertiesItemList;

    /** Constructor */
    public ScreenEditorPropertiesScreen(){
        super();

        propertiesItemList = new ListBox<Setting<?>>(1508, 10, 404, 1060){
            @Override
            public CompositeUIElement makeRenderElementForItem(Setting<?> item) {
                return item.makeUIFor(0, 0, width, 100);
            }
        }.setTitle("Properties:");
        propertiesItemList.getBackground().setImage(null);
        addInteractableElement(propertiesItemList);

        hide();
    }

    public void createPropertiesFor(ScreenEditorItem item){
        propertiesItemList.setItems(item.getPropertiesForItem());
    }

    public void clearScreen(){
        propertiesItemList.clearItems();
    }
}
