package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.data.implementations.RenderableData;
import dLib.ui.elements.UIElement;
import dLib.util.TextureManager;

public class Renderable extends UIElement {
    protected int width = 0;
    protected int height = 0;

    protected Texture image;

    protected Color renderColor;

    protected boolean visible = true;

    public Renderable(Texture image){
        this(image, 0, 0);
    }

    public Renderable(Texture image, int xPos, int yPos){
        this(image, xPos, yPos, image.getWidth(), image.getHeight());
    }

    public Renderable(Texture image, int xPos, int yPos, int width, int height){
        super(xPos, yPos);

        this.image = image;
        this.width = width;
        this.height = height;
        this.renderColor = Color.WHITE.cpy();
    }

    public Renderable(RenderableData renderableData){
        super(renderableData);

        this.image = TextureManager.getTexture(renderableData.texturePath);
        this.width = renderableData.width;
        this.height = renderableData.height;
        this.renderColor = Color.valueOf(renderableData.color);
    }

    /** Builder methods */
    public Renderable setWidth(int newWidth){
        this.width = newWidth;
        return this;
    }

    public Renderable setHeight(int newHeight){
        this.height = newHeight;
        return this;
    }

    public Renderable setSize(int newWidth, int newHeight){
        setWidth(newWidth);
        setHeight(newHeight);
        return this;
    }

    public Renderable setImage(Texture image){
        this.image = image;
        return this;
    }

    public Renderable setRenderColor(Color color){
        this.renderColor = color;
        return this;
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

    /** Misc methods */
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public Texture getImage() { return image; }

    @Override
    public void setVisibility(boolean visible) {
        this.visible = visible;
    }
    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setEnabled(boolean enabled) {
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isActive() {
        return isVisible() || isEnabled();
    }
}
