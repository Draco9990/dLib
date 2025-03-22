package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.properties.objects.ColorProperty;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.color.ColorPickerPopup;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
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

        middleButton = new Button(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(50)){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                ColorPickerPopup colorPickerPopup = new ColorPickerPopup(boundProperty.getColorValue(), Pos.px((int) (InputHelper.mX / Settings.xScale - 340)), Pos.px((int) (InputHelper.mY / Settings.yScale)), true, true, false);
                colorPickerPopup.onSelectedColorChangedEvent.subscribe(colorPickerPopup, color -> property.setValue(color.toString()));
                colorPickerPopup.open();
            }
        };
        middleButton.setTexture(Tex.stat(UICommonResources.white_pixel));
        middleButton.setRenderColor(property.getColorValue());

        property.onValueChangedEvent.subscribe(this, (oldColor, newColor) -> {
            if(!isEditorValidForPropertyChange()) return;

            middleButton.setRenderColor(Color.valueOf(newColor));
        });

        addChild(middleButton);
    }


    //endregion
}
