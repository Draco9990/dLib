package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UITheme;
import dLib.util.ColorHelpers;
import dLib.util.TextureManager;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ColorPicker extends UIElement {
    //region Variables

    private ColorWheel colorWheel;

    private Interactable lightnessBar;

    private float lightness;

    //endregion

    //region Constructors

    public ColorPicker(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);

        int colorWheelHeight = (int) (height * 0.8f);
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
                float mX = (InputHelper.mX / Settings.xScale) - x;
                colorWheel.setLightness(mX / width);
            }
        };
        addChildNCS(lightnessBar);

        addChildNCS(new Renderable(TextureManager.getTexture("dLibResources/images/ui/LightnessBar.png"), lightnessBar.getPositionX(), lightnessBar.getPositionY(), lightnessBar.getWidth(), lightnessBar.getHeight()));
    }

    //endregion

    //region Methods

    public ColorWheel getColorWheel(){
        return colorWheel;
    }

    //endregion

    public static class ColorWheel extends Interactable {
        //region Variables

        private Texture colorWheelTexture;

        private ArrayList<Consumer<Color>> colorHoveredConsumers = new ArrayList<>();
        private ArrayList<Consumer<Color>> colorSelectedConsumers = new ArrayList<>();

        private float lightness = 0.5f;

        //endregion

        //region Constructors

        public ColorWheel(int x, int y, int width, int height) {
            super(null, x, y, width, height);
            recreateTexture();
        }

        //endregion

        //region Methods

        //region Dimensions

        @Override
        public ColorWheel setDimensions(Integer newWidth, Integer newHeight) {
            super.setDimensions(newWidth, newHeight);
            recreateTexture();
            return this;
        } //TODO RF add a parent on scale changed callback

        //endregion

        //region Render Texture & Color

        @Override
        protected Texture getTextureForRender() {
            return colorWheelTexture;
        }

        @Override
        protected Color getColorForRender() {
            return Color.WHITE.cpy();
        }

        //endregion

        //region Lightness

        public ColorWheel setLightness(float newLightness){
            lightness = newLightness;
            recreateTexture();
            return this;
        }

        //endregion

        //region Triggers

        //region Left Click

        @Override
        protected void onLeftClick() {
            super.onLeftClick();
            Color currentColor = getCurrentColor();
            if(currentColor != null) onColorSelected(getCurrentColor());
        }

        @Override
        protected void onLeftClickHeld(float totalDuration) {
            super.onLeftClickHeld(totalDuration);
            if(isHovered()){
                Color currentColor = getCurrentColor();
                if(currentColor != null) onColorSelected(getCurrentColor());
            }
        }

        @Override
        protected void onHoverTick(float totalTickDuration) {
            super.onHoverTick(totalTickDuration);
            Color currentColor = getCurrentColor();
            if(currentColor != null) onColorHovered(getCurrentColor());
        }

        //endregion

        //endregion

        //region Color Selection

        public void onColorHovered(Color color){
            for(Consumer<Color> consumer : colorHoveredConsumers) consumer.accept(color);
        }
        public ColorWheel addColorHoveredConsumer(Consumer<Color> consumer){
            this.colorHoveredConsumers.add(consumer);
            return this;
        }

        public void onColorSelected(Color color){
            for(Consumer<Color> consumer : colorSelectedConsumers) consumer.accept(color);
        }
        public ColorWheel addColorSelectedConsumer(Consumer<Color> consumer) {
            this.colorSelectedConsumers.add(consumer);
            return this;
        }

        private Color getCurrentColor(){
            int radius = Math.min(width, height) / 2;
            float dx = InputHelper.mX / Settings.xScale - (x + (float)width / 2);
            float dy = InputHelper.mY / Settings.yScale - (y + (float)height / 2);
            float hue = (float) ((Math.atan2(-dy, dx) / Math.PI / 2 + 1) % 1);

            if (dx * dx + dy * dy <= radius * radius) {
                return ColorHelpers.fromHSL(hue, 1, lightness);
            }

            return null;
        }


        //endregion

        private void recreateTexture() {
            int radius = Math.min(width, height) / 2;
            Pixmap pixmap = new Pixmap(radius * 2, radius * 2, Pixmap.Format.RGBA8888);
            for (int dy = -radius; dy < radius; dy++) {
                for (int dx = -radius; dx < radius; dx++) {
                    if (dx * dx + dy * dy <= radius * radius) {
                        float hue = (float) ((Math.atan2(dy, dx) / Math.PI / 2 + 1) % 1); // Ensure hue is within [0, 1)
                        pixmap.setColor(ColorHelpers.fromHSL(hue, 1, lightness));
                        pixmap.drawPixel(dx + radius, dy + radius);
                    }
                }
            }
            colorWheelTexture = new Texture(pixmap);
            pixmap.dispose();
        }


        //endregion
    }
}
