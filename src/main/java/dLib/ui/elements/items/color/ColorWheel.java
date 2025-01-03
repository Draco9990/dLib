package dLib.ui.elements.items.color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.items.Renderable;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.events.Event;
import dLib.util.helpers.ColorHelpers;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

import java.util.function.Consumer;

public class ColorWheel extends Renderable {
    public Event<Consumer<Color>> onColorHoveredEvent = new Event<>();
    public Event<Consumer<Color>> onColorSelectedEvent = new Event<>();

    public ColorWheel(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(new TextureNoneBinding(), xPos, yPos, width, height);

        recreateTexture();
    }

    //region Methods

    //region Texture

    @Override
    public void onDimensionsChanged() {
        super.onDimensionsChanged();

        recreateTexture();
    }

    private void recreateTexture(){
        int radius = Math.min(getWidth(), getHeight()) / 2;
        Pixmap pixmap = new Pixmap(radius * 2, radius * 2, Pixmap.Format.RGBA8888);
        for (int dy = -radius; dy < radius; dy++) {
            for (int dx = -radius; dx < radius; dx++) {
                if (dx * dx + dy * dy <= radius * radius) {
                    float hue = (float) ((Math.atan2(dy, dx) / Math.PI / 2 + 1) % 1); // Ensure hue is within [0, 1)
                    pixmap.setColor(ColorHelpers.fromHSL(hue, 1, 0.5f));
                    pixmap.drawPixel(dx + radius, dy + radius);
                }
            }
        }

        setImage(Tex.stat(new Texture(pixmap)));

        pixmap.dispose();
    }

    //endregion Texture

    //region Selection

    @Override
    protected void onHoverTick(float totalTickDuration) {
        super.onHoverTick(totalTickDuration);

        Color newColor = calculateCurrentColor();
        if(newColor != null){
            onColorHoveredEvent.invoke(consumer -> consumer.accept(newColor));
        }
    }

    @Override
    protected void onLeftClick() {
        super.onLeftClick();

        Color newColor = calculateCurrentColor();
        if(newColor != null){
            onColorSelectedEvent.invoke(consumer -> consumer.accept(newColor));
        }
    }

    private Color calculateCurrentColor(){
        int radius = Math.min(getWidth(), getHeight()) / 2;
        float dx = InputHelper.mX / Settings.xScale - (getWorldPositionX() + (float)getWidth() / 2);
        float dy = InputHelper.mY / Settings.yScale - (getWorldPositionY() + (float)getHeight() / 2);
        float hue = (float) ((Math.atan2(-dy, dx) / Math.PI / 2 + 1) % 1);

        if (dx * dx + dy * dy <= radius * radius) {
            return ColorHelpers.fromHSL(hue, 1, 0.5f);
        }

        return null;
    }

    //endregion Selection

    //endregion
}
