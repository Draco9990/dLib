package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.properties.objects.ColorProperty;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.screens.ColorPickerScreen;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class ColorValueEditor extends AbstractValueEditor<Color, ColorProperty> {
    //region Variables

    Button middleButton;

    //endregion

    //region Constructors

    public ColorValueEditor(Color color) {
        this(new ColorProperty(color));
    }

    public ColorValueEditor(ColorProperty property) {
        super(property);

        middleButton = (Button) new Button(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(50)){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                ColorPickerScreen colorPickerScreen = new ColorPickerScreen(middleButton.getRenderColor()){
                    @Override
                    public void onColorChosen(Color color) {
                        super.onColorChosen(color);
                        boundProperty.setValue(color.toString());
                    }
                };
                colorPickerScreen.open();
            }
        };
        middleButton.setImage(Tex.stat(UICommonResources.white_pixel));
        middleButton.setRenderColor(property.getColorValue());

        property.onValueChangedEvent.subscribe(this, (oldColor, newColor) -> {
            if(!isEditorValidForPropertyChange()) return;

            middleButton.setRenderColor(Color.valueOf(newColor));
        });

        addChildNCS(middleButton);
    }


    //endregion
}
