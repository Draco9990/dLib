package dLib.properties.ui.elements;

import dLib.properties.objects.EnumProperty;
import dLib.ui.elements.items.ComboBox;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.helpers.EnumHelpers;
import dLib.util.ui.dimensions.Dim;

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
            leftArrow = new Button(Dim.mirror(), Dim.px(50));
            leftArrow.setTexture(Tex.stat(UICommonResources.arrow_left));
            leftArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.previous());
            leftArrow.setControllerSelectable(false);
            box.addChild(leftArrow);

            enumBox = new ComboBox<OfType>(boundProperty.getValue(), EnumHelpers.getAllEntries(boundProperty.getValue()), Dim.fill(), Dim.px(50)){
                @Override
                public String itemToString(OfType item) {
                    return EnumHelpers.betterToString(item);
                }
            };
            enumBox.onSelectionChangedEvent.subscribe(this, (Consumer<OfType>) (newVal) -> {
                boundProperty.setValue(newVal);
            });
            box.addChild(enumBox);

            rightArrow = new Button(Dim.mirror(), Dim.px(50));
            rightArrow.setTexture(Tex.stat(UICommonResources.arrow_right));
            rightArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.next());
            rightArrow.setControllerSelectable(false);
            box.addChild(rightArrow);
        }
        addChild(box);

        setControllerSelectable(true);

        property.onValueChangedEvent.subscribe(this, (oldValue, newValue) -> {
            if(!isEditorValidForPropertyChange()) return;

            enumBox.label.setText(boundProperty.getValueForDisplay());
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
