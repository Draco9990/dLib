package dLib.ui.elements.items;

import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;

import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ComboBox<OptionType> extends TextButton {
    private boolean canBeNull = false;

    private OptionType currentOption;

    public Event<Consumer<OptionType>> onSelectionChangedEvent = new Event<>();

    public ComboBox(OptionType initialOption, ArrayList<OptionType> options, AbstractDimension width, AbstractDimension height) {
        this(initialOption, options, Pos.px(0), Pos.px(0), width, height);
    }
    public ComboBox(OptionType initialOption, ArrayList<OptionType> options, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(initialOption.toString(), xPos, yPos, width, height);

        getTextBox().setText(itemToStringShort(initialOption));

        getButton().onLeftClickEvent.subscribeManaged(() -> {
            int buttonX = getButton().getWorldPositionX();
            int buttonY = getButton().getWorldPositionY();

            SimpleListPicker<OptionType> picker = new SimpleListPicker<OptionType>(buttonX, buttonY, options, this::setSelectedItem) {
                @Override
                public String itemToString(OptionType item) {
                    return ComboBox.this.itemToStringLong(item);
                }
            };
            picker.open();
        });
        getButton().setImage(Tex.stat(UICommonResources.button02_horizontal));
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
        getTextBox().setText(itemToStringShort(option));

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
