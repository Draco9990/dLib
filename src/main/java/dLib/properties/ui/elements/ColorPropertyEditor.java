package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.themes.UITheme;
import dLib.util.screens.ColorPickerScreen;
import dLib.properties.objects.templates.TColorProperty;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class ColorPropertyEditor extends AbstractPropertyEditor<TColorProperty<?>> {
    //region Variables

    Button middleButton;

    //endregion

    //region Constructors

    public ColorPropertyEditor(TColorProperty setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TColorProperty property, AbstractDimension width, AbstractDimension height) {
        middleButton = (Button) new Button(Pos.px(0), Pos.px(0), width, height){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                ColorPickerScreen colorPickerScreen = new ColorPickerScreen(property.getColorValue()){
                    @Override
                    public void onColorChosen(Color color) {
                        super.onColorChosen(color);
                        property.setColorValue(color);
                    }
                };
                colorPickerScreen.open();
            }
        }.setImage(UITheme.whitePixel).setRenderColor(property.getColorValue());

        property.addOnValueChangedListener((color, color2) -> middleButton.setRenderColor(property.getColorValue()));

        return middleButton;
    }

    //endregion
}
