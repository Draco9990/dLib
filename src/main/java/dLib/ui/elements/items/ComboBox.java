package dLib.ui.elements.items;

import dLib.ui.annotations.DisplayClass;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.string.Str;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;

public class ComboBox<OptionType> extends TextButton {
    private boolean canBeNull = false;

    private OptionType currentOption;
    public ArrayList<OptionType> options;

    public ConsumerEvent<OptionType> onSelectionChangedEvent = new ConsumerEvent<>();

    public ComboBox(OptionType initialOption, ArrayList<OptionType> options, AbstractDimension width, AbstractDimension height) {
        this(initialOption, options, Pos.px(0), Pos.px(0), width, height);
    }
    public ComboBox(OptionType initialOption, ArrayList<OptionType> options, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(initialOption.toString(), xPos, yPos, width, height);

        this.options = options;

        currentOption = initialOption;

        label.setText(itemToStringShort(initialOption));

        postLeftClickEvent.subscribeManaged(() -> {
            float buttonX = getWorldPositionX();
            float buttonY = getWorldPositionY();

            SimpleListPicker<OptionType> picker = new SimpleListPicker<OptionType>(buttonX, buttonY, this.options) {
                @Override
                public String itemToString(OptionType item) {
                    return ComboBox.this.itemToStringLong(item);
                }
            };
            picker.onOptionSelectedEvent.subscribeManaged(this::setSelectedItem);
            picker.open();
        });
        setTexture(Tex.stat(UICommonResources.button02_square));

        setSayTheSpireElementType("Combo box");
        setSayTheSpireElementName((String)null);
        setSayTheSpireElementValue(Str.lambda(this::getText));
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

        onSelectionChangedEvent.invoke(option);
    }
    public OptionType getCurrentlySelectedItem(){
        return currentOption;
    }

    public String itemToString(OptionType item){
        if(item == null) return "None";
        if(item.getClass().isAnnotationPresent(DisplayClass.class)) return item.getClass().getAnnotation(DisplayClass.class).shortDisplayName();
        return item.toString();
    }

    public String itemToStringLong(OptionType item){
        if(item == null) return "None";
        return itemToString(item);
    }

    public String itemToStringShort(OptionType item){
        if(item == null) return "None";
        if(item.getClass().isAnnotationPresent(DisplayClass.class)) return item.getClass().getAnnotation(DisplayClass.class).shortDisplayName();
        return itemToString(item);
    }
}
