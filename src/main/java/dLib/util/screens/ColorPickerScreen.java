package dLib.util.screens;

import com.badlogic.gdx.graphics.Color;
import dLib.DLib;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.ColorWheel;
import dLib.ui.elements.prefabs.ColorWheelWithLightness;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.screens.ScreenManager;
import dLib.ui.themes.UITheme;

import java.util.function.Consumer;

public class ColorPickerScreen extends AbstractScreen {
    /** Variables */
    private Color currentColor;

    private Renderable hoverPreview;
    private Renderable selectedPreview;

    /** Constructors */
    public ColorPickerScreen(AbstractScreen invoker, Color initialColor){
        setScreenToOpenOnClose(invoker);

        addGenericBackground();

        ColorWheelWithLightness colorWheel = new ColorWheelWithLightness(235, 1080-654, 550, 550);
        colorWheel.getColorWheel().setColorHoveredConsumer(new Consumer<Color>() {
            @Override
            public void accept(Color color) {
                hoverPreview.setRenderColor(color);
            }
        });
        colorWheel.getColorWheel().setColorSelectedConsumer(new Consumer<Color>() {
            @Override
            public void accept(Color color) {
                onColorSelectedChanged(color);
            }
        });
        addElement(colorWheel);

        hoverPreview = new Renderable(UITheme.whitePixel, 273, 1080-596, 475, 33);
        addElement(hoverPreview);

        selectedPreview = new Renderable(UITheme.whitePixel, 50, 1080-850, 1810, 80);
        addElement(selectedPreview);

        TextButton confirmButton = new TextButton("CONFIRM", 685, 1080-1015, 550, 150);
        confirmButton.getButton().setOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                onColorChosen(currentColor);
                ScreenManager.closeScreen();
            }
        });
        addElement(confirmButton);

        onColorSelectedChanged(initialColor);
    }

    private void onColorSelectedChanged(Color newColor){
        currentColor = newColor;
        selectedPreview.setRenderColor(currentColor);
    }

    public void onColorChosen(Color color){

    }

    /** ID */
    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
