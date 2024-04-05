package dLib.propertyeditors.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.screens.ScreenManager;
import dLib.ui.themes.UITheme;
import dLib.util.screens.ColorPickerScreen;
import dLib.util.settings.prefabs.ColorProperty;

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
                ScreenManager.openScreen(new ColorPickerScreen(ScreenManager.getCurrentScreen(), property.getValue()){
                    @Override
                    public void onColorChosen(Color color) {
                        super.onColorChosen(color);
                        property.setValue(color);
                    }
                });
            }
        }.setImage(UITheme.whitePixel).setRenderColor(property.getValue());

        property.addOnValueChangedListener((color, color2) -> middleButton.setRenderColor(property.getValue()));

        return middleButton;
    }

    //endregion
}
