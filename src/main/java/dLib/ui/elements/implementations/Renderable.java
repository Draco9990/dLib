package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.data.implementations.RenderableData;
import dLib.ui.elements.UIElement;

public class Renderable extends UIElement {
    protected Texture image;

    protected Color renderColor;

    public Renderable(Texture image){
        this(image, 0, 0);
    }

    public Renderable(Texture image, int xPos, int yPos){
        this(image, xPos, yPos, image.getWidth(), image.getHeight());
    }

    public Renderable(Texture image, int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);

        this.image = image;
        this.renderColor = Color.WHITE.cpy();
    }

    public Renderable(RenderableData renderableData){
        super(renderableData);

        this.image = renderableData.textureBinding.getBoundTexture();
    }

    /** Update and render */
    @Override
    public void update() {

    }

    @Override
    public void render(SpriteBatch sb) {
        if(!shouldRender()) return;

        sb.setColor(getColorForRender());

        Texture textureToRender = getTextureForRender();
        if(textureToRender != null){
            sb.draw(textureToRender, x * Settings.xScale, y * Settings.yScale, width * Settings.xScale, height * Settings.yScale);
        }

        sb.setColor(Color.WHITE);
    }

    protected Texture getTextureForRender(){
        return image;
    }
    protected Color getColorForRender(){
        return renderColor;
    }

    /** Image */
    public Renderable setImage(Texture image){
        this.image = image;
        return this;
    }
    public Texture getImage() { return image; }

    /** Color */
    public Renderable setRenderColor(Color color){
        this.renderColor = color;
        return this;
    }

    /** Enabled/Visible/Active */
    @Override
    public void setEnabled(boolean enabled) {
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
