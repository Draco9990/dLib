package dLib.ui.elements.prefabs;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SimpleListPicker<OptionType> extends UIElement {
    private VerticalListBox<OptionType> listBox;
    private Consumer<OptionType> onOptionSelected;

    public SimpleListPicker(int right, int top, ArrayList<OptionType> options, Consumer<OptionType> inOnOptionSelected) {
        super(Pos.px(right), Pos.px(top), Dim.px(400), Dim.px(300));

        setContextual(true);

        setHorizontalAlignment(Alignment.HorizontalAlignment.RIGHT);
        setVerticalAlignment(Alignment.VerticalAlignment.TOP);

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
        };
        listBox.setItems(options);
        addChildNCS(listBox);
    }

    @Override
    public int getWidth() {
        int res = super.getWidth();
        return res;
    }

    @Override
    public int getHeight() {
        int res = super.getHeight();
        return res;
    }

    @Override
    public int getLocalPositionX() {
        int res = super.getLocalPositionX();
        return res;
    }

    @Override
    public int getLocalPositionY() {
        int res = super.getLocalPositionY();
        return res;
    }
}
