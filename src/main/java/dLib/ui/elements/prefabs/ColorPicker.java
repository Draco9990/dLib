package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.implementations.Renderable;

import dLib.util.ColorHelpers;
import dLib.util.TextureManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ColorPicker extends UIElement {
    //region Variables

    private ColorWheel colorWheel;

    private Interactable lightnessBar;

    private float lightness;

    //endregion

    //region Constructors

    public ColorPicker(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public ColorPicker(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public ColorPicker(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        /*int colorWheelHeight = (int) (height * 0.8f);
        int lightnessHeight = (int) (height * 0.1f);
        int colorWheelWidth = width;
        if(width == height){
            colorWheelWidth = colorWheelHeight;
        }

        int diffX = width - colorWheelWidth;
        int diffY = height - colorWheelHeight;

        colorWheel = new ColorWheel((int)(diffX * 0.5f), diffY, colorWheelWidth, colorWheelHeight){
            @Override
            public void onColorSelected(Color color) {
                super.onColorSelected(color);
                float[] hsl = ColorHelpers.toHSL(color);
                lightnessBar.setRenderColor(ColorHelpers.fromHSL(hsl[0], hsl[1], 0.5f));
            }
        };
        addChildNCS(colorWheel);

        lightnessBar = new Interactable(UITheme.whitePixel, 0, 0, width, lightnessHeight){
            @Override
            protected Color getColorForRender() {
                return renderColor;
            }

            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                float mX = (InputHelper.mX / Settings.xScale) - getWorldPositionX();
                colorWheel.setLightness(mX / getWidth());
            }
        };
        addChildNCS(lightnessBar);

        addChildNCS(new Renderable(TextureManager.getTexture("dLibResources/images/ui/LightnessBar.png"), 0, 0, lightnessBar.g(), lightnessBar.getHeight()));*/
    }

    //endregion

    //region Methods

    public ColorWheel getColorWheel(){
        return colorWheel;
    }

    //endregion
}
