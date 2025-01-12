package dLib.ui.elements.items;

import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ComboBox<OptionType> extends TextButton {
    private boolean canBeNull = false;

    private OptionType currentOption;
    private ArrayList<OptionType> options;

    public Event<Consumer<OptionType>> onSelectionChangedEvent = new Event<>();

    public ComboBox(OptionType initialOption, ArrayList<OptionType> options, AbstractDimension width, AbstractDimension height) {
        this(initialOption, options, Pos.px(0), Pos.px(0), width, height);
    }
    public ComboBox(OptionType initialOption, ArrayList<OptionType> options, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(initialOption.toString(), xPos, yPos, width, height);

        this.options = options;

        label.setText(itemToStringShort(initialOption));

        onLeftClickEvent.subscribeManaged(() -> {
            int buttonX = getWorldPositionX();
            int buttonY = getWorldPositionY();

            SimpleListPicker<OptionType> picker = new SimpleListPicker<OptionType>(buttonX, buttonY, this.options) {
                @Override
                public String itemToString(OptionType item) {
                    return ComboBox.this.itemToStringLong(item);
                }
            };
            picker.onOptionSelectedEvent.subscribeManaged(this::setSelectedItem);
            picker.open();
        });
        setImage(Tex.stat(UICommonResources.button02_horizontal));
    }

    public void setCanBeNull(boolean canBeNull) {
        this.canBeNull = canBeNull;
    }
    public boolean canBeNull() {
        return canBeNull;
    }

    public void setSelectedItem(OptionType option){
        if(option == currentOption) return;
        if(option == null && !canBeNull) return;

        currentOption = option;
        label.setText(itemToStringShort(option));

        onSelectionChangedEvent.invoke(optionTypeConsumer -> optionTypeConsumer.accept(option));
    }
    public OptionType getCurrentlySelectedItem(){
        return currentOption;
    }

    public String itemToString(OptionType item){
        if(item == null) return "None";
        return item.toString();
    }

    public String itemToStringLong(OptionType item){
        if(item == null) return "None";
        return itemToString(item);
    }

    public String itemToStringShort(OptionType item){
        if(item == null) return "None";
        return itemToString(item);
    }
}
