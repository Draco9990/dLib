package dLib.properties.ui.elements;

import dLib.properties.objects.EnumProperty;
import dLib.ui.elements.items.Button;
import dLib.ui.elements.items.ComboBox;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.EnumHelpers;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

import java.util.ArrayList;
import java.util.function.Consumer;

public class EnumValueEditor<OfType extends Enum<OfType>> extends AbstractValueEditor<OfType, EnumProperty<OfType>> {
    //region Variables

    Button leftArrow;
    Button rightArrow;

    ComboBox enumBox;

    //endregion

    //region Constructors

    public EnumValueEditor(OfType value){
        this(new EnumProperty(value));
    }

    public EnumValueEditor(EnumProperty<OfType> property) {
        super(property);

        HorizontalBox box = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            leftArrow = new Button(Dim.height(), Dim.px(50));
            leftArrow.setImage(Tex.stat(UICommonResources.arrow_left));
            leftArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.previous());
            box.addItem(leftArrow);

            enumBox = new ComboBox<OfType>(boundProperty.getValue(), EnumHelpers.getAllEntries(boundProperty.getValue()), Dim.fill(), Dim.px(50)){
                @Override
                public String itemToString(OfType item) {
                    return EnumHelpers.betterToString(item);
                }
            };
            enumBox.onSelectionChangedEvent.subscribe(this, (Consumer<OfType>) (newVal) -> {
                boundProperty.setValue(newVal);
            });
            box.addItem(enumBox);

            rightArrow = new Button(Dim.height(), Dim.px(50));
            rightArrow.setImage(Tex.stat(UICommonResources.arrow_right));
            rightArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.next());
            box.addItem(rightArrow);
        }
        addChildNCS(box);

        property.onValueChangedEvent.subscribe(this, (oldValue, newValue) -> {
            if(!isEditorValidForPropertyChange()) return;

            enumBox.getTextBox().setText(boundProperty.getValueForDisplay());
        });
    }

    //endregion

    //region Methods

    @Override
    public boolean onLeftInteraction() {
        return leftArrow.onConfirmInteraction();
    }

    @Override
    public boolean onRightInteraction() {
        return rightArrow.onConfirmInteraction();
    }

    @Override
    public boolean onConfirmInteraction() {
        return enumBox.onConfirmInteraction();
    }

    //endregion
}
