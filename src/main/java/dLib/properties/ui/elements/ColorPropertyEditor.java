package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.themes.UITheme;
import dLib.util.screens.ColorPickerScreen;
import dLib.properties.objects.ColorProperty;

public class ColorPropertyEditor extends AbstractPropertyEditor<ColorProperty> {
    //region Variables

    Button middleButton;

    //endregion

    //region Constructors

    public ColorPropertyEditor(ColorProperty setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(ColorProperty property, Integer width, Integer height) {
        middleButton = (Button) new Button(0, 0, width, height){
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
