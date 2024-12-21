package dLib.ui.elements.items;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.VerticalListBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SimpleListPicker<OptionType> extends Renderable {
    private VerticalListBox<OptionType> listBox;
    private Consumer<OptionType> onOptionSelected;

    public SimpleListPicker(int right, int top, ArrayList<OptionType> options, Consumer<OptionType> inOnOptionSelected) {
        super(Tex.stat(ImageMaster.OPTION_CONFIRM), Pos.px(right-400), Pos.px(top-300), Dim.px(530), Dim.px(315));

        setContextual(true);

        onOptionSelected = inOnOptionSelected;

        Scrollbox scrollbox = new Scrollbox(Pos.px(35), Pos.px(40), Dim.px(465), Dim.px(250));
        scrollbox.setIsHorizontal(false);
        {
            listBox = new VerticalListBox<OptionType>(Dim.fill(), Dim.fill()){
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
            scrollbox.addChildNCS(listBox);
        }
        addChildNCS(scrollbox);
    }

    public String itemToString(OptionType item) {
        return item.toString();
    }
}
