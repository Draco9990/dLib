package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.resources.UICommonResources;
import dLib.util.screens.ColorPickerScreen;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.Pos;

public class ColorValueEditor extends AbstractValueEditor<Color> {
    //region Variables

    Button middleButton;

    //endregion

    //region Constructors

    public ColorValueEditor(Color color, AbstractDimension width, AbstractDimension height) {
        super(width, height);

        middleButton = (Button) new Button(Pos.px(0), Pos.px(0), width, height){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                ColorPickerScreen colorPickerScreen = new ColorPickerScreen(middleButton.getRenderColor()){
                    @Override
                    public void onColorChosen(Color color) {
                        super.onColorChosen(color);
                        setValueEvent.invoke(objectConsumer -> objectConsumer.accept(color));
                    }
                };
                colorPickerScreen.open();
            }
        }.setImage(UICommonResources.white_pixel).setRenderColor(color);

        onValueChangedEvent.subscribe(this, (newColor) -> middleButton.setRenderColor(newColor));

        addChildNCS(middleButton);
    }


    //endregion
}
