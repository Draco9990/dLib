package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.elements.UIElement;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;

import java.io.Serializable;

public class Renderable extends UIElement {
    //region Variables

    protected Texture image;

    protected Color renderColor;

    //endregion

    //region Constructors

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

    public Renderable(RenderableData data){
        super(data);

        this.image = data.textureBinding.getBoundTexture();
        this.renderColor = Color.valueOf(data.renderColor);
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void renderSelf(SpriteBatch sb) {
        super.renderSelf(sb);

        sb.setColor(getColorForRender());

        Texture textureToRender = getTextureForRender();
        if(textureToRender != null){
            sb.draw(textureToRender, getWorldPositionX() * Settings.xScale, getWorldPositionY() * Settings.yScale, width * Settings.xScale, height * Settings.yScale);
        }

        sb.setColor(Color.WHITE);
    }

    //endregion

    //region Image

    public Renderable setImage(Texture image){
        this.image = image;
        return this;
    }
    public Texture getImage() { return image; }

    protected Texture getTextureForRender(){
        return image;
    }

    //endregion

    //region Render Color

    public Renderable setRenderColor(Color color){
        this.renderColor = color;
        return this;
    }
    public Color getRenderColor(Color color){
        return renderColor;
    }

    protected Color getColorForRender(){
        return renderColor;
    }

    //endregion

    //endregion

    public static class RenderableData extends UIElement.UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public TextureBinding textureBinding = new TextureEmptyBinding();

        public String renderColor = Color.WHITE.cpy().toString();

        @Override
        public UIElement makeUIElement() {
            return new Renderable(this);
        }
    }
}
