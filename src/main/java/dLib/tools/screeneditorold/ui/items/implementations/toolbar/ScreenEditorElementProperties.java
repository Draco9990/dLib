package dLib.tools.screeneditorold.ui.items.implementations.toolbar;

import dLib.tools.screeneditorold.ui.items.editoritems.ScreenEditorItem;
import dLib.ui.elements.items.itembox.VerticalListBox;
import dLib.properties.objects.templates.TProperty;
import dLib.util.bindings.texture.TextureNoneBinding;

public class ScreenEditorElementProperties extends AbstractScreenEditorToolbar {
    //region Variables

    private VerticalListBox<TProperty<?, ?>> propertiesItemList;
    private ScreenEditorItem propertiesFor;

    //endregion

    //region Constructors

    public ScreenEditorElementProperties(){
        super();

        propertiesItemList.setImage(new TextureNoneBinding());
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
