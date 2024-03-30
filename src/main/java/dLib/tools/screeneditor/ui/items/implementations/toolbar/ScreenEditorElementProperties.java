package dLib.tools.screeneditor.ui.items.implementations.toolbar;

import dLib.tools.screeneditor.ui.items.editoritems.ScreenEditorItem;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.util.ESelectionMode;
import dLib.util.Reflection;
import dLib.util.settings.Property;

public class ScreenEditorElementProperties extends AbstractScreenEditorToolbar {
    //region Variables

    private ListBox<Property<?>> propertiesItemList;
    private ScreenEditorItem propertiesFor;

    //endregion

    //region Constructors

    public ScreenEditorElementProperties(){
        super();

        propertiesItemList = new ListBox<Property<?>>(0, 0, getWidth(), getHeight()){
            @Override
            public UIElement makeUIForItem(Property<?> item) {
                return (UIElement) Reflection.invokeMethod("makeEditUI", item, 0, 0, width, 100);
            }
        }.setSelectionMode(ESelectionMode.NONE).setTitle("Properties:");
        propertiesItemList.getBackground().setImage(null);
        propertiesItemList.setItemSpacing(25);
        addChildNCS(propertiesItemList);

        hideAndDisable();
    }

    //endregion

    //region Methods

    public void createPropertiesFor(ScreenEditorItem<?, ?> item){
        propertiesFor = item;
        propertiesItemList.setItems(item.getItemProperties());
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
