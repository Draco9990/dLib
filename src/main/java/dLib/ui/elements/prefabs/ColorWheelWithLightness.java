package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UITheme;
import dLib.util.ColorHelpers;
import dLib.util.TextureManager;

public class ColorWheelWithLightness extends CompositeUIElement {
    /** Variables  */
    private ColorWheel colorWheel;

    private Interactable lightnessBar;

    private float lightness;

    /** Constructors */
    public ColorWheelWithLightness(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);

        int colorWheelHeight = (int) (height * 0.8f);
        int lightnessHeight = (int) (height * 0.1f);
        int colorWheelWidth = width;
        if(width == height){
            colorWheelWidth = colorWheelHeight;
        }

        int diffX = width - colorWheelWidth;
        int diffY = height - colorWheelHeight;

        colorWheel = new ColorWheel((int)(xPos + diffX * 0.5f), yPos + diffY, colorWheelWidth, colorWheelHeight){
            @Override
            public void onColorSelected(Color color) {
                super.onColorSelected(color);
                float[] hsl = ColorHelpers.toHSL(color);
                lightnessBar.setRenderColor(ColorHelpers.fromHSL(hsl[0], hsl[1], 0.5f));
            }
        };
        foreground.add(colorWheel);

        lightnessBar = new Interactable(UITheme.whitePixel, xPos, yPos, width, lightnessHeight){
            @Override
            protected Color getColorForRender() {
                return renderColor;
            }

            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                float mX = (InputHelper.mX / Settings.xScale) - x;
                colorWheel.setLightness(mX / width);
            }
        };
        middle = lightnessBar;
        foreground.add(new Renderable(TextureManager.getTexture("dLibResources/images/ui/LightnessBar.png"), lightnessBar.getPositionX(), lightnessBar.getPositionY(), lightnessBar.getWidth(), lightnessBar.getHeight()));
    }

    /** Color Wheel */
    public ColorWheel getColorWheel(){
        return colorWheel;
    }
}
