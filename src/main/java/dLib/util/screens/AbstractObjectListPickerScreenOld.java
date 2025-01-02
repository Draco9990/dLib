package dLib.util.screens;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.VerticalListBox;

import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class AbstractObjectListPickerScreenOld<ItemPickType> extends UIElement { //TODO RF
    /** Variables */
    private Consumer<ItemPickType> onItemSelected;

    /** Constructors */
    public AbstractObjectListPickerScreenOld(ArrayList<ItemPickType> itemsToPick){
        super(Pos.px(0), Pos.px(0), Dim.px(1920), Dim.px(1080));
        AbstractObjectListPickerScreenOld<ItemPickType> instance = this;

        VerticalListBox<ItemPickType> mainBox = new VerticalListBox<ItemPickType>(Pos.px(40), Pos.px(1080 - 915), Dim.px(1850), Dim.px(875)){
            @Override
            public void onItemSelectionChanged(ArrayList<ItemPickType> items) {
                super.onItemSelectionChanged(items);

                if(items.isEmpty()) return;
                instance.onItemSelected(items.get(0));
                instance.close();
            }
        };
        mainBox.setItems(itemsToPick);
        addChild(mainBox);
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
