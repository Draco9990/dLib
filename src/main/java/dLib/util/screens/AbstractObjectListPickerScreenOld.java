package dLib.util.screens;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Image;
import dLib.ui.elements.prefabs.VerticalListBox;
import dLib.ui.screens.AbstractScreen_DEPRECATED;
import dLib.ui.themes.UIThemeManager;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class AbstractObjectListPickerScreenOld<ItemPickType> extends UIElement {
    /** Variables */
    private Consumer<ItemPickType> onItemSelected;

    /** Constructors */
    public AbstractObjectListPickerScreenOld(ArrayList<ItemPickType> itemsToPick){
        super(0, 0, 1920, 1080);
        AbstractObjectListPickerScreenOld<ItemPickType> instance = this;

        addChildNCS(new Image(UIThemeManager.getDefaultTheme().background, 0, 0, getWidth(), getHeight()));
        addChildCS(new VerticalListBox<ItemPickType>(40, 1080 - 915, 1850, 875){
            @Override
            public void onItemSelectionChanged(ArrayList<ItemPickType> items) {
                super.onItemSelectionChanged(items);

                if(items.isEmpty()) return;
                instance.onItemSelected(items.get(0));
                instance.close();
            }
        }.setItems(itemsToPick));
    }

    /** Item Pick Results */
    public void onItemSelected(ItemPickType item){
        if(onItemSelected != null) onItemSelected.accept(item);
    }
    public AbstractObjectListPickerScreenOld<ItemPickType> setOnItemSelectedConsumer(Consumer<ItemPickType> consumer){
        onItemSelected = consumer;
        return this;
    }
}
