package dLib.ui.elements.prefabs;

import dLib.properties.objects.templates.TEnumProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class EnumBoxWithArrows extends UIElement {
    public Button leftArrow;
    public Button rightArrow;

    public TextButton enumBox;

    private TEnumProperty<?, ?> linkedProperty;

    public EnumBoxWithArrows(AbstractDimension width, AbstractDimension height) {
        this(Pos.perc(0), Pos.perc(0), width, height);
    }
    public EnumBoxWithArrows(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        leftArrow = new Button(Pos.perc(0), Pos.perc(0), Dim.perc(0.25), Dim.fill());
        leftArrow.setImage(UIThemeManager.getDefaultTheme().arrow_left);
        addChildNCS(leftArrow);

        enumBox = new TextButton("", Pos.perc(0.25), Pos.perc(0), Dim.fill(), Dim.fill());
        enumBox.getButton().setImage(UIThemeManager.getDefaultTheme().button_large_square);
        addChildNCS(enumBox);

        rightArrow = new Button(Pos.perc(0.75), Pos.perc(0), Dim.height(), Dim.fill());
        rightArrow.setImage(UIThemeManager.getDefaultTheme().arrow_right);
        addChildNCS(rightArrow);
    }

    //region Methods

    public EnumBoxWithArrows setLinkedProperty(TEnumProperty<?, ?> property) {
        enumBox.getTextBox().setText(property.getValueForDisplay());

        linkedProperty = property;

        leftArrow.addOnLeftClickConsumer(property::previous);
        rightArrow.addOnLeftClickConsumer(property::next);

        enumBox.getButton().addOnLeftClickConsumer(property::next);

        property.addOnValueChangedListener((integer, integer2) -> {
            if(!enumBox.getTextBox().getText().equals(property.getValue().toString())){
                enumBox.getTextBox().setText(property.getValueForDisplay());
            }
        });

        return this;
    }

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

    public enum ENumberType{
        WHOLE,
        DECIMAL
    }
}
