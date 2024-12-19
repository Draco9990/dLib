package dLib.properties.ui.elements;

import dLib.properties.objects.EnumProperty;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
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

        HorizontalBox box = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            leftArrow = new Button(Dim.height(), Dim.px(50));
            leftArrow.setImage(Tex.stat(UICommonResources.arrow_left));
            leftArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.previous());
            box.addItem(leftArrow);

            enumBox = new TextButton(boundProperty.getValueForDisplay(), Dim.fill(), Dim.px(50));
            enumBox.getButton().setImage(Tex.stat(UICommonResources.button02_horizontal));
            enumBox.getButton().onLeftClickEvent.subscribe(this, () -> boundProperty.next());
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
