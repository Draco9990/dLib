package dLib.properties.ui.elements;

import dLib.properties.objects.EnumProperty;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class EnumValueEditor extends AbstractValueEditor<Enum<?>, EnumProperty> {
    //region Variables

    Button leftArrow;
    Button rightArrow;

    TextButton enumBox;

    //endregion

    //region Constructors

    public EnumValueEditor(Enum<?> value){
        this(new EnumProperty(value));
    }

    public EnumValueEditor(EnumProperty<?> property) {
        super(property);

        {
            leftArrow = new Button(Pos.px(0), Pos.px(0), Dim.perc(0.25), Dim.px(50));
            leftArrow.setImage(UICommonResources.arrow_left);
            leftArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.previous());
            addChildNCS(leftArrow);

            enumBox = new TextButton(boundProperty.getValueForDisplay(), Pos.perc(0.25), Pos.px(0), Dim.fill(), Dim.px(50));
            enumBox.getButton().setImage(UICommonResources.button02_horizontal);
            enumBox.getButton().onLeftClickEvent.subscribe(this, () -> boundProperty.next());
            addChildNCS(enumBox);

            rightArrow = new Button(Pos.perc(0.75), Pos.px(0), Dim.height(), Dim.px(50));
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
