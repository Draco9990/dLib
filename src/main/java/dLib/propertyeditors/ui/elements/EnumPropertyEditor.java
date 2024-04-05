package dLib.propertyeditors.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.themes.UIThemeManager;
import dLib.util.settings.prefabs.EnumProperty;

public class EnumPropertyEditor extends AbstractPropertyEditor<EnumProperty<? extends Enum<?>>> {
    //region Variables

    Button leftArrow;
    Button rightArrow;

    TextButton middleButton;

    //endregion

    //region Constructors

    public EnumPropertyEditor(EnumProperty<? extends Enum<?>> setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(EnumProperty<? extends Enum<?>> property, Integer width, Integer height) {
        HorizontalBox box = new HorizontalBox(0, 0, width, height);

        int arrowDim = Math.min((int)(0.2f * width), height);

        leftArrow = new Button(0, 0, arrowDim, arrowDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                property.previous();
            }
        }.setImage(UIThemeManager.getDefaultTheme().arrow_left);
        box.addItem(leftArrow);

        rightArrow = new Button(0, 0, arrowDim, arrowDim){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                property.next();
            }
        }.setImage(UIThemeManager.getDefaultTheme().arrow_right);
        box.addItem(rightArrow);

        middleButton = new TextButton(property.getValueForDisplay(), 0, 0, width - arrowDim * 2, height);
        box.addItem(middleButton);

        property.addOnValueChangedListener(() -> {
            if(!middleButton.getTextBox().getText().equals(property.getValueForDisplay())){
                middleButton.getTextBox().setText(property.getValueForDisplay());
            }
        });

        return box;
    }

    @Override
    public boolean onLeftInteraction() {
        leftArrow.trigger();
        return true;
    }

    @Override
    public boolean onRightInteraction() {
        rightArrow.trigger();
        return true;
    }

    //endregion
}
