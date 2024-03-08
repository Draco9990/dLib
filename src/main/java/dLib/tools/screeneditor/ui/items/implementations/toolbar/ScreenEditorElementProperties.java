package dLib.tools.screeneditor.ui.items.implementations.toolbar;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.util.ESelectionMode;
import dLib.util.settings.Setting;

public class ScreenEditorElementProperties extends AbstractScreenEditorToolbar {
    //region Variables

    private ListBox<Setting<?>> propertiesItemList;
    private ScreenEditorItem propertiesFor;

    //endregion

    //region Constructors

    public ScreenEditorElementProperties(){
        super();

        propertiesItemList = new ListBox<Setting<?>>(0, 0, getWidth(), getHeight()){
            @Override
            public CompositeUIElement makeUIForItem(Setting<?> item) {
                return item.makeUIFor(0, 0, width, 100);
            }
        }.setSelectionMode(ESelectionMode.NONE).setTitle("Properties:");
        propertiesItemList.getBackground().setImage(null);
        propertiesItemList.setItemSpacing(25);
        addChildNCS(propertiesItemList);

        hide();
    }

    //endregion

    //region Methods

    public void createPropertiesFor(ScreenEditorItem item){
        propertiesFor = item;
        propertiesItemList.setItems(item.getPropertiesForItem());
    }

    public void clearScreen(){
        propertiesFor = null;
        propertiesItemList.clearItems();
    }

    @Override
    protected void onRefreshElement() {
        super.onRefreshElement();
        if(propertiesFor != null){
            createPropertiesFor(propertiesFor);
        }
    }

    //endregion
}
