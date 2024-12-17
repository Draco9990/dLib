package dLib.properties.ui.elements;

import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.EnumHelpers;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class EnumValueEditor extends AbstractValueEditor<Enum<?>> {
    //region Variables

    Button leftArrow;
    Button rightArrow;

    TextButton enumBox;

    Enum<?> currentValue;

    //endregion

    //region Constructors

    public EnumValueEditor(Enum<?> value, AbstractDimension width, AbstractDimension height) {
        super(width, height);

        this.currentValue = value;

        {
            leftArrow = new Button(Pos.px(0), Pos.px(0), Dim.perc(0.25), Dim.fill());
            leftArrow.setImage(UICommonResources.arrow_left);
            leftArrow.onLeftClickEvent.subscribe(this, () -> {
                setValueEvent.invoke(objectConsumer -> objectConsumer.accept(EnumHelpers.previousEnum(currentValue)));
            });
            addChildNCS(leftArrow);

            enumBox = new TextButton("", Pos.perc(0.25), Pos.px(0), Dim.fill(), Dim.fill());
            enumBox.getButton().setImage(UICommonResources.button02_horizontal);
            enumBox.getButton().onLeftClickEvent.subscribe(this, () -> {
                setValueEvent.invoke(objectConsumer -> objectConsumer.accept(EnumHelpers.nextEnum(currentValue)));
            });
            addChildNCS(enumBox);

            rightArrow = new Button(Pos.perc(0.75), Pos.px(0), Dim.height(), Dim.fill());
            rightArrow.setImage(UICommonResources.arrow_right);
            rightArrow.onLeftClickEvent.subscribe(this, () -> {
                setValueEvent.invoke(objectConsumer -> objectConsumer.accept(EnumHelpers.nextEnum(currentValue)));
            });
            addChildNCS(rightArrow);
        }

        onValueChangedEvent.subscribe(this, (newValue) -> {
            if(!enumBox.getTextBox().getText().equals(newValue.toString())){
                enumBox.getTextBox().setText(newValue.toString());
            }
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
