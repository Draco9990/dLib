package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import dLib.properties.objects.ColorProperty;
import dLib.ui.elements.UIElement;
import dLib.util.bindings.texture.TextureEmptyBinding;
import dLib.properties.objects.TextureBindingProperty;

import java.io.Serializable;

public class Renderable extends UIElement {
    //region Variables

    protected TextureRegion image;

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

        this.image = image == null ? null : new TextureRegion(image);
        this.renderColor = Color.WHITE.cpy();
    }

    public Renderable(RenderableData data){
        super(data);

        Texture texture = data.textureBinding.getValue().getBoundTexture();
        this.image = texture == null ? null : new TextureRegion(texture);
        this.renderColor = data.renderColor.getColorValue();
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void renderSelf(SpriteBatch sb) {
        super.renderSelf(sb);

        sb.setColor(getColorForRender());

        TextureRegion textureToRender = getTextureForRender();
        if(textureToRender != null){
            sb.draw(textureToRender, getWorldPositionX() * Settings.xScale, getWorldPositionY() * Settings.yScale, getWidth() * Settings.xScale, getHeight() * Settings.yScale);
        }

        sb.setColor(Color.WHITE);
    }

    //endregion

    //region Image

    public Renderable setImage(Texture image){
        if(image == null) this.image = null;
        else this.image = new TextureRegion(image);
        return this;
    }
    public Texture getImage() { return image.getTexture(); }

    protected TextureRegion getTextureForRender(){
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

        public TextureBindingProperty textureBinding = new TextureBindingProperty(new TextureEmptyBinding()).setName("Image");

        public ColorProperty renderColor = (ColorProperty) new ColorProperty(Color.WHITE.cpy()).setName("Render Color");

        @Override
        public UIElement makeUIElement() {
            return new Renderable(this);
        }
    }
}
