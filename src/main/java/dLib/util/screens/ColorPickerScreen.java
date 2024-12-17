package dLib.util.screens;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.ColorPicker;
import dLib.ui.elements.prefabs.Image;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.resources.UICommonResources;


import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.function.Consumer;

public class ColorPickerScreen extends UIElement {
    /** Variables */
    private Color currentColor;

    private Renderable hoverPreview;
    private Renderable selectedPreview;

    /** Constructors */
    public ColorPickerScreen(Color initialColor){
        super(Pos.px(0), Pos.px(0), Dim.px(1920), Dim.px(1080));

        ColorPickerScreen self = this;

        ColorPicker colorWheel = new ColorPicker(Pos.px(235), Pos.px(1080-654), Dim.px(550), Dim.px(550));
        colorWheel.getColorWheel().addColorHoveredConsumer(new Consumer<Color>() {
            @Override
            public void accept(Color color) {
                hoverPreview.setRenderColor(color);
            }
        });
        colorWheel.getColorWheel().addColorSelectedConsumer(new Consumer<Color>() {
            @Override
            public void accept(Color color) {
                onColorSelectedChanged(color);
            }
        });
        addChildNCS(colorWheel);

        hoverPreview = new Renderable(Tex.stat(UICommonResources.white_pixel), Pos.px(273), Pos.px(1080-596), Dim.px(475), Dim.px(33));
        addChildNCS(hoverPreview);

        selectedPreview = new Renderable(Tex.stat(UICommonResources.white_pixel), Pos.px(50), Pos.px(1080-850), Dim.px(1810), Dim.px(80));
        addChildNCS(selectedPreview);

        TextButton confirmButton = new TextButton("CONFIRM", Pos.px(685), Pos.px(1080-1015), Dim.px(550), Dim.px(150));
        confirmButton.getButton().onLeftClickEvent.subscribeManaged(() -> {
            onColorChosen(currentColor);
            self.close();
        });
        addChildCS(confirmButton);

        onColorSelectedChanged(initialColor);
    }

    private void onColorSelectedChanged(Color newColor){
        currentColor = newColor;
        selectedPreview.setRenderColor(currentColor);
    }

    public void onColorChosen(Color color){

    }
}
