package dLib.util.screens;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.ColorPicker;
import dLib.ui.elements.prefabs.Image;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.AbstractScreen_DEPRECATED;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;

import java.util.function.Consumer;

public class ColorPickerScreen extends UIElement {
    /** Variables */
    private Color currentColor;

    private Renderable hoverPreview;
    private Renderable selectedPreview;

    /** Constructors */
    public ColorPickerScreen(Color initialColor){
        super(0, 0, 1920, 1080);

        ColorPickerScreen self = this;

        addChildNCS(new Image(UIThemeManager.getDefaultTheme().background, 0, 0, getWidth(), getHeight()));

        ColorPicker colorWheel = new ColorPicker(235, 1080-654, 550, 550);
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

        hoverPreview = new Renderable(UITheme.whitePixel, 273, 1080-596, 475, 33);
        addChildNCS(hoverPreview);

        selectedPreview = new Renderable(UITheme.whitePixel, 50, 1080-850, 1810, 80);
        addChildNCS(selectedPreview);

        TextButton confirmButton = new TextButton("CONFIRM", 685, 1080-1015, 550, 150);
        confirmButton.getButton().addOnLeftClickConsumer(() -> {
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
