package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UITheme;
import dLib.util.ColorHelpers;
import dLib.util.TextureManager;

import java.util.function.Consumer;

public class ColorPicker extends CompositeUIElement {
    /** Variables  */
    private ColorWheel colorWheel;

    private Interactable lightnessBar;

    private float lightness;

    /** Constructors */
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

    public static class ColorWheel extends Interactable {
        /** Variables */
        private Texture colorWheelTexture;

        private Consumer<Color> colorHoveredConsumer;
        private Consumer<Color> colorSelectedConsumer;

        private float lightness = 0.5f;

        /** Constructors */
        public ColorWheel(int x, int y, int width, int height) {
            super(null, x, y, width, height);
            recreateTexture();
        }

        /** Position & Dimensions*/
        @Override
        public ColorWheel setPosition(Integer newPosX, Integer newPosY) {
            super.setPosition(newPosX, newPosY);
            recreateTexture();
            return this;
        }

        @Override
        public ColorWheel setDimensions(Integer newWidth, Integer newHeight) {
            super.setDimensions(newWidth, newHeight);
            recreateTexture();
            return this;
        }

        /** Update and Render */
        @Override
        public void render(SpriteBatch sb) {
            super.render(sb);
            sb.setColor(Color.WHITE.cpy());
            sb.draw(colorWheelTexture, x * Settings.xScale, y * Settings.yScale, width * Settings.xScale, height * Settings.yScale);
        }

        /** Lightness */
        public ColorWheel setLightness(float newLightness){
            lightness = newLightness;
            recreateTexture();
            return this;
        }

        /** Left Click */
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

        /** Texture */
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

        /** Color Selection */
        public void onColorHovered(Color color){
            if(colorHoveredConsumer != null){
                colorHoveredConsumer.accept(color);
            }
        }
        public ColorWheel setColorHoveredConsumer(Consumer<Color> consumer){
            this.colorHoveredConsumer = consumer;
            return this;
        }

        public void onColorSelected(Color color){
            if(colorSelectedConsumer != null){
                colorSelectedConsumer.accept(color);
            }
        }
        public ColorWheel setColorSelectedConsumer(Consumer<Color> consumer) {
            this.colorSelectedConsumer = consumer;
            return this;
        }
    }
}
