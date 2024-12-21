package dLib.ui.elements.items;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.VerticalListBox;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SimpleListPicker<OptionType> extends UIElement {
    private VerticalListBox<OptionType> listBox;
    private Consumer<OptionType> onOptionSelected;

    public SimpleListPicker(int right, int top, ArrayList<OptionType> options, Consumer<OptionType> inOnOptionSelected) {
        super(Pos.px(right-400), Pos.px(top-300), Dim.px(400), Dim.px(300));

        setContextual(true);

        onOptionSelected = inOnOptionSelected;

        listBox = new VerticalListBox<OptionType>(Pos.px(10), Pos.px(10), Dim.px(380), Dim.px(280)){
            @Override
            public void onItemSelectionChanged(ArrayList<OptionType> items) {
                super.onItemSelectionChanged(items);

                if(!items.isEmpty()){
                    onOptionSelected.accept(items.get(0));

                    SimpleListPicker parent = getParentOfType(SimpleListPicker.class);
                    parent.close();
                }
            }

            @Override
            public String itemToString(OptionType item) {
                return SimpleListPicker.this.itemToString(item);
            }
        };
        listBox.setItems(options);
        addChildNCS(listBox);
    }

    public String itemToString(OptionType item) {
        return item.toString();
    }
}
