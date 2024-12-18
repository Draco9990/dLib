package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import dLib.properties.objects.*;
import dLib.ui.elements.UIElement;
import dLib.ui.resources.UICommonResources;
import dLib.util.IntegerVector2;
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

    private boolean preserveAspectRatio = false;
    private boolean noUpscale = false;

    private IntegerVector2 renderOffset = new IntegerVector2(0, 0);
    private Vector2 renderScaleOffset = new Vector2(1, 1);

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

        this.renderOffset = data.positionOffset.getValue();
        this.renderScaleOffset = data.renderScaleOffset.getValue();
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

            float renderPosX = getWorldPositionX() * Settings.xScale;
            float renderPosY = getWorldPositionY() * Settings.yScale;
            float renderWidth = getWidth() * Settings.xScale * getRenderWidthPerc();
            float renderHeight = getHeight() * Settings.yScale * getRenderHeightPerc();

            if(isPreservingAspectRatio()){
                float aspectRatio = (float)textureToRender.getRegionWidth() / (float)textureToRender.getRegionHeight();
                if(aspectRatio > 1){
                    renderHeight = renderWidth / aspectRatio;
                }
                else {
                    renderWidth = renderHeight * aspectRatio;
                }

                //Adjust render position to center the image
                renderPosX += (getWidth() * Settings.xScale - renderWidth) / 2;
                renderPosY += (getHeight() * Settings.yScale - renderHeight) / 2;
            }

            if(isNoUpscale()){
                if(renderWidth > textureToRender.getRegionWidth()){
                    renderWidth = textureToRender.getRegionWidth();
                }
                if(renderHeight > textureToRender.getRegionHeight()){
                    renderHeight = textureToRender.getRegionHeight();
                }

                //Adjust render position to center the image
                renderPosX += (getWidth() * Settings.xScale - renderWidth) / 2;
                renderPosY += (getHeight() * Settings.yScale - renderHeight) / 2;
            }

            renderPosX += renderOffset.x;
            renderPosY += renderOffset.y;

            renderWidth *= renderScaleOffset.x;
            renderHeight *= renderScaleOffset.y;

            sb.draw(textureToRender, renderPosX, renderPosY, renderWidth, renderHeight);

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

    //region Aspect Ratio

    public Renderable setPreserveAspectRatio(boolean preserveAspectRatio){
        this.preserveAspectRatio = preserveAspectRatio;
        return this;
    }

    public boolean isPreservingAspectRatio(){
        return preserveAspectRatio;
    }

    //endregion

    //region No Upscale

    public Renderable setNoUpscale(boolean noUpscale){
        this.noUpscale = noUpscale;
        return this;
    }

    public boolean isNoUpscale(){
        return noUpscale;
    }

    //endregion

    //region Scale Offsets

    public Renderable setRenderOffset(IntegerVector2 offset){
        renderOffset = offset;
        return this;
    }

    public IntegerVector2 getRenderOffset(){
        return renderOffset;
    }

    public Renderable setRenderScaleOffset(Vector2 offset){
        renderScaleOffset = offset;
        return this;
    }

    public Vector2 getRenderScaleOffset(){
        return renderScaleOffset;
    }

    //endregion Scale offsets

    //endregion

    public static class RenderableData extends UIElement.UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public TextureBindingProperty textureBinding = new TextureBindingProperty(new TextureResourceBinding(UICommonResources.class, "white_pixel"))
                .setName("Image")
                .setDescription("Image to render as this element.")
                .setCategory("Render");

        public IntegerVector2Property positionOffset = new IntegerVector2Property(new IntegerVector2(0, 0))
                .setName("X Position Offset")
                .setDescription("Offset for the render position of the rendered image.")
                .setCategory("Render")
                .setValueNames("X", "Y");

        public FloatVector2Property renderScaleOffset = new FloatVector2Property(new Vector2(1, 1))
                .setName("Render Scale Offset")
                .setDescription("Offset for the scale of the rendered image.")
                .setCategory("Render")
                .setValueNames("W", "H");

        public ColorProperty renderColor = new ColorProperty(Color.WHITE.cpy()).setName("Render Color");

        public float renderColorAlphaMultiplier = 1.0f;

        public FloatVector2Property renderDimensionsPerc = new FloatVector2Property(new Vector2(1, 1)).setName("Render Dimensions Perc").setValueNames("W", "H").setMinimumX(0).setMinimumY(0).setMaximumX(1).setMaximumY(1);

        @Override
        public UIElement makeUIElement() {
            return new Renderable(this);
        }
    }
}
