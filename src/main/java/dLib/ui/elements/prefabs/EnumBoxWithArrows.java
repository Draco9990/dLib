package dLib.ui.elements.prefabs;

import dLib.properties.objects.templates.TEnumProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;

public class EnumBoxWithArrows extends UIElement {
    public Button leftArrow;
    public Button rightArrow;

    public TextButton enumBox;

    private TEnumProperty<?, ?> linkedProperty;

    public EnumBoxWithArrows(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);

        int arrowDims = height;
        if(arrowDims * 2 > width){
            throw new IllegalArgumentException("Width must be at least twice the height");
        }

        int actualArrowDims = (int) (arrowDims / 2.0f);
        int arrowPadding = (int) (actualArrowDims / 2.0f);
        leftArrow = new Button(arrowPadding, arrowPadding, actualArrowDims, actualArrowDims);
        leftArrow.setImage(UIThemeManager.getDefaultTheme().arrow_left);
        addChildNCS(leftArrow);

        enumBox = new TextButton("", arrowDims, 0, width - arrowDims * 2, height);
        enumBox.getButton().setImage(UIThemeManager.getDefaultTheme().button_large_square);
        addChildNCS(enumBox);

        rightArrow = new Button(width - arrowDims + arrowPadding, arrowPadding, actualArrowDims, actualArrowDims);
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
