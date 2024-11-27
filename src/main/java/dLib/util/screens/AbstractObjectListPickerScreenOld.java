package dLib.util.screens;

import dLib.ui.elements.prefabs.VerticalListBox;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.screens.UIManager;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class AbstractObjectListPickerScreenOld<ItemPickType> extends AbstractScreen {
    /** Variables */
    private Consumer<ItemPickType> onItemSelected;

    /** Constructors */
    public AbstractObjectListPickerScreenOld(ArrayList<ItemPickType> itemsToPick){
        AbstractObjectListPickerScreenOld<ItemPickType> instance = this;

        addGenericBackground();
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
