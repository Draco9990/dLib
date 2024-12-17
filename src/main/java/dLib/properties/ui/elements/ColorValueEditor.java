package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.properties.objects.ColorProperty;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.resources.UICommonResources;
import dLib.util.screens.ColorPickerScreen;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.Pos;

public class ColorValueEditor extends AbstractValueEditor<Color, ColorProperty> {
    //region Variables

    Button middleButton;

    //endregion

    //region Constructors

    public ColorValueEditor(Color color, AbstractDimension width, AbstractDimension height) {
        this(new ColorProperty(color), width, height);
    }

    public ColorValueEditor(ColorProperty property, AbstractDimension width, AbstractDimension height) {
        super(property, width, height);

        middleButton = (Button) new Button(Pos.px(0), Pos.px(0), width, height){
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
        }.setImage(UICommonResources.white_pixel).setRenderColor(property.getColorValue());

        property.onValueChangedEvent.subscribe(this, (oldColor, newColor) -> {
            if(!isEditorValidForPropertyChange()) return;

            middleButton.setRenderColor(Color.valueOf(newColor));
        });

        addChildNCS(middleButton);
    }


    //endregion
}
