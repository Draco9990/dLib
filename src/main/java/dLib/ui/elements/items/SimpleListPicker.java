package dLib.ui.elements.items;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.ui.elements.items.itembox.VerticalDataBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SimpleListPicker<OptionType> extends Renderable {
    private VerticalDataBox<OptionType> listBox;

    public Event<Consumer<OptionType>> onOptionSelectedEvent = new Event<>();

    public SimpleListPicker(int right, int top, ArrayList<OptionType> options) {
        super(Tex.stat(ImageMaster.OPTION_CONFIRM), Pos.px(right-400), Pos.px(top-300), Dim.px(530), Dim.px(315));

        setContextual(true);

        Scrollbox scrollbox = new Scrollbox(Pos.px(35), Pos.px(40), Dim.px(465), Dim.px(250));
        scrollbox.setIsHorizontal(false);
        {
            listBox = new VerticalDataBox<OptionType>(Dim.fill(), Dim.fill()){
                @Override
                public void onItemSelectionChanged() {
                    super.onItemSelectionChanged();

                    ArrayList<OptionType> selectedItems = getCurrentlySelectedItems();
                    if(!selectedItems.isEmpty()){
                        onOptionSelectedEvent.invoke(optionTypeConsumer -> optionTypeConsumer.accept(selectedItems.get(0)));

                        SimpleListPicker parent = getParentOfType(SimpleListPicker.class);
                        parent.close();
                    }
                }

                @Override
                public String itemToString(OptionType item) {
                    return SimpleListPicker.this.itemToString(item);
                }
            };
            listBox.setChildren(options);
            scrollbox.addChild(listBox);
        }
        addChild(scrollbox);
    }

    public String itemToString(OptionType item) {
        return item.toString();
    }
}
