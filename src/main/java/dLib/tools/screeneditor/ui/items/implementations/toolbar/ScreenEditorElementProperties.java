package dLib.tools.screeneditor.ui.items.implementations.toolbar;

import dLib.tools.screeneditor.ui.items.editoritems.ScreenEditorItem;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.VerticalListBox;
import dLib.ui.util.ESelectionMode;
import dLib.properties.objects.templates.TProperty;

public class ScreenEditorElementProperties extends AbstractScreenEditorToolbar {
    //region Variables

    private VerticalListBox<TProperty<?, ?>> propertiesItemList;
    private ScreenEditorItem propertiesFor;

    //endregion

    //region Constructors

    public ScreenEditorElementProperties(){
        super();

        propertiesItemList = (VerticalListBox<TProperty<?, ?>>) new VerticalListBox<TProperty<?, ?>>(0, 0, getWidth(), getHeight()){
            @Override
            public UIElement makeUIForItem(TProperty<?, ?> item) {
                return item.makePropertyEditor(0, 0, getWidth(), 50);
            }
        }.setSelectionMode(ESelectionMode.NONE).setTitle("Properties:").disableItemWrapping();
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

    //endregion
}
