package dLib.util.screens;

import dLib.ui.elements.prefabs.HorizontalListBox;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.screens.ScreenManager;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class AbstractObjectListPickerScreen<ItemPickType> extends AbstractScreen {
    /** Variables */
    private Consumer<ItemPickType> onItemSelected;

    /** Constructors */
    public AbstractObjectListPickerScreen(AbstractScreen caller, ArrayList<ItemPickType> itemsToPick){
        AbstractObjectListPickerScreen<ItemPickType> instance = this;

        setScreenToOpenOnClose(caller);

        addGenericBackground();
        addChildCS(new HorizontalListBox<ItemPickType>(40, 1080 - 915, 1850, 875){
            @Override
            public void onItemSelectionChanged(ArrayList<ItemPickType> items) {
                super.onItemSelectionChanged(items);

                if(items.isEmpty()) return;
                instance.onItemSelected(items.get(0));
                ScreenManager.closeScreen();
            }
        }.setItems(itemsToPick));
    }

    /** Item Pick Results */
    public void onItemSelected(ItemPickType item){
        if(onItemSelected != null) onItemSelected.accept(item);
    }
    public AbstractObjectListPickerScreen<ItemPickType> setOnItemSelectedConsumer(Consumer<ItemPickType> consumer){
        onItemSelected = consumer;
        return this;
    }
}
