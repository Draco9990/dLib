package dLib.properties.ui.elements;

import dLib.properties.objects.EnumProperty;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.EnumHelpers;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class EnumValueEditor extends AbstractValueEditor<Enum<?>, EnumProperty> {
    //region Variables

    Button leftArrow;
    Button rightArrow;

    TextButton enumBox;

    //endregion

    //region Constructors

    public EnumValueEditor(Enum<?> value, AbstractDimension width, AbstractDimension height){
        this(new EnumProperty(value), width, height);
    }

    public EnumValueEditor(EnumProperty<?> property, AbstractDimension width, AbstractDimension height) {
        super(property, width, height);

        {
            leftArrow = new Button(Pos.px(0), Pos.px(0), Dim.perc(0.25), Dim.fill());
            leftArrow.setImage(UICommonResources.arrow_left);
            leftArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.previous());
            addChildNCS(leftArrow);

            enumBox = new TextButton(boundProperty.getValueForDisplay(), Pos.perc(0.25), Pos.px(0), Dim.fill(), Dim.fill());
            enumBox.getButton().setImage(UICommonResources.button02_horizontal);
            enumBox.getButton().onLeftClickEvent.subscribe(this, () -> boundProperty.next());
            addChildNCS(enumBox);

            rightArrow = new Button(Pos.perc(0.75), Pos.px(0), Dim.height(), Dim.fill());
            rightArrow.setImage(UICommonResources.arrow_right);
            rightArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.next());
            addChildNCS(rightArrow);
        }

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
