package dLib.ui.elements.prefabs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.ColorHelpers;

public class ColorPicker extends UIElement {
    /** Variables */
    private Texture colorWheelTexture;

    /** Constructors */
    public ColorPicker(int x, int y, int width, int height) {
        super(x, y, width, height);

        recreateTexture();
    }

    /** Position & Dimensions*/
    @Override
    public ColorPicker setPosition(Integer newPosX, Integer newPosY) {
        super.setPosition(newPosX, newPosY);
        recreateTexture();
        return this;
    }

    @Override
    public ColorPicker setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);
        recreateTexture();
        return this;
    }

    /** Update and Render */
    @Override
    public void update() {
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE.cpy());
        sb.draw(colorWheelTexture, x * Settings.xScale, y * Settings.yScale, width * Settings.xScale, height * Settings.yScale);
    }

    /** Texture */
    private void recreateTexture() {
        int radius = Math.min(width, height) / 2;
        Pixmap pixmap = new Pixmap(radius * 2, radius * 2, Pixmap.Format.RGBA8888);
        for (int dy = -radius; dy < radius; dy++) {
            for (int dx = -radius; dx < radius; dx++) {
                if (dx * dx + dy * dy <= radius * radius) {
                    float hue = (float) ((Math.atan2(dy, dx) / Math.PI / 2 + 1) % 1); // Ensure hue is within [0, 1)
                    pixmap.setColor(ColorHelpers.fromHSV(hue, 1, 1));
                    pixmap.drawPixel(dx + radius, dy + radius);
                }
            }
        }
        colorWheelTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    //TODO ON COLOR PICKED

    /*@Override
    public void dispose() {
        batch.dispose();
        colorWheelTexture.dispose();
    }*/
}
