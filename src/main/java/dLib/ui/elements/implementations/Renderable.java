package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import dLib.properties.objects.ColorProperty;
import dLib.properties.objects.FloatVector2Property;
import dLib.properties.objects.TextureBindingProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureResourceBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class Renderable extends UIElement {
    //region Variables

    protected TextureBinding image;

    protected Color renderColor;

    private float renderColorAlphaMultiplier = 1.0f;

    protected Vector2 renderDimensionsPerc;

    //endregion

    //region Constructors

    public Renderable(TextureBinding imageBinding){
        this(imageBinding, Pos.px(0), Pos.px(0));
    }
    public Renderable(TextureBinding imageBinding, AbstractPosition xPos, AbstractPosition yPos){
        this(imageBinding, xPos, yPos, Dim.px(imageBinding.getBoundObject().getRegionWidth()), Dim.px(imageBinding.getBoundObject().getRegionHeight()));
    }
    public Renderable(TextureBinding imageBinding, AbstractDimension width, AbstractDimension height){
        this(imageBinding, Pos.px(0), Pos.px(0), width, height);
    }
    public Renderable(TextureBinding imageBinding, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(xPos, yPos, width, height);

        this.image = imageBinding;
        this.renderColor = Color.WHITE.cpy();

        this.renderDimensionsPerc = new Vector2(1.f, 1.f);

        setPassthrough(false);
    }

    public Renderable(RenderableData data){
        super(data);

        this.image = data.textureBinding.getValue();
        this.renderColor = data.renderColor.getColorValue();

        this.renderColorAlphaMultiplier = data.renderColorAlphaMultiplier;

        this.renderDimensionsPerc = data.renderDimensionsPerc.getValue();
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void renderSelf(SpriteBatch sb) {
        sb.setColor(getColorForRender());

        TextureRegion textureToRender = getTextureForRender();
        if(textureToRender != null){
            textureToRender.setRegionWidth((int)(textureToRender.getTexture().getWidth() * getRenderWidthPerc()));
            textureToRender.setRegionHeight((int)(textureToRender.getTexture().getHeight() * getRenderHeightPerc()));
            sb.draw(textureToRender, getWorldPositionX() * Settings.xScale, getWorldPositionY() * Settings.yScale, getWidth() * Settings.xScale * getRenderWidthPerc(), getHeight() * Settings.yScale * getRenderHeightPerc());
            sb.flush();  //* We have to flush after drawing because ScissorStack only applies to the last drawn elements for some reason
        }

        super.renderSelf(sb);
    }

    //endregion

    //region Image

    public void setImage(TextureBinding image){
        this.image = image;
    }

    protected TextureRegion getTextureForRender(){
        return image.getBoundObject();
    }

    //endregion

    //region Render Color

    public Renderable setRenderColor(Color color){
        this.renderColor = color;
        return this;
    }
    public Color getRenderColor(){
        return renderColor;
    }

    protected Color getColorForRender(){
        if(isDarkened()){
            Color cpy = getDarkenedColor().cpy();
            cpy = cpy.lerp(renderColor, 1 - getDarkenedColorMultiplier());
            cpy.a *= getRenderColorAlphaMultiplier();
            return cpy;
        }
        else {
            Color renderColor = this.renderColor.cpy();
            renderColor.a *= getRenderColorAlphaMultiplier();
            return renderColor;
        }
    }

    public void setRenderColorAlphaMultiplier(float newAlpha){
        renderColorAlphaMultiplier = newAlpha;
    }

    public float getRenderColorAlphaMultiplier(){
        return renderColorAlphaMultiplier;
    }

    //endregion

    //region Render Width & Height Percentage

    public Renderable setRenderWidthPerc(float perc){
        setRenderDimensionsPerc(perc, renderDimensionsPerc.y);
        return this;
    }

    public float getRenderWidthPerc(){
        return renderDimensionsPerc.x;
    }

    public Renderable setRenderHeightPerc(float perc){
        setRenderDimensionsPerc(renderDimensionsPerc.x, perc);
        return this;
    }

    public float getRenderHeightPerc(){
        return renderDimensionsPerc.y;
    }

    public Renderable setRenderDimensionsPerc(float widthPerc, float heightPerc){
        if(widthPerc < 0) widthPerc = 0;
        if(widthPerc > 1) widthPerc = 1;

        if(heightPerc < 0) heightPerc = 0;
        if(heightPerc > 1) heightPerc = 1;

        this.renderDimensionsPerc.set(widthPerc, heightPerc);
        return this;
    }

    public Vector2 getRenderDimensionsPerc(){
        return renderDimensionsPerc;
    }

    //endregion

    //endregion

    public static class RenderableData extends UIElement.UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public TextureBindingProperty textureBinding = new TextureBindingProperty(new TextureResourceBinding(UICommonResources.class, "white_pixel"))
                .setName("Image")
                .setDescription("Image to render as this element.")
                .setCategory("Render");

        public ColorProperty renderColor = new ColorProperty(Color.WHITE.cpy()).setName("Render Color");

        public float renderColorAlphaMultiplier = 1.0f;

        public FloatVector2Property renderDimensionsPerc = new FloatVector2Property(new Vector2(1, 1)).setName("Render Dimensions Perc").setValueNames("W", "H").setMinimumX(0).setMinimumY(0).setMaximumX(1).setMaximumY(1);

        @Override
        public UIElement makeUIElement() {
            return new Renderable(this);
        }
    }
}
