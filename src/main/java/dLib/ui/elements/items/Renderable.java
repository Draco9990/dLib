package dLib.ui.elements.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.megacrit.cardcrawl.core.Settings;
import dLib.properties.objects.*;
import dLib.shaders.ShaderManager;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.TextureResourceBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;
import java.util.ArrayList;

public class Renderable extends UIElement {
    //region Variables

    protected AbstractTextureBinding image;

    protected Color renderColor;

    private float renderColorAlphaMultiplier = 1.0f;

    protected Alignment renderOrientation = new Alignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER);

    protected Vector2 renderDimensionsPerc;
    protected Alignment renderDimensionsOrientation = new Alignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.BOTTOM);

    private boolean preserveAspectRatio = false;
    private boolean noUpsize = false;

    private Vector2 renderOffset = new Vector2(0, 0);
    private Vector2 renderScaleOffset = new Vector2(1, 1);

    private int hueShiftAmount = 0;

    //endregion

    //region Constructors

    public Renderable(AbstractTextureBinding imageBinding){
        this(imageBinding, Pos.px(0), Pos.px(0));
    }
    public Renderable(AbstractTextureBinding imageBinding, AbstractPosition xPos, AbstractPosition yPos){
        this(imageBinding, xPos, yPos, Dim.px(imageBinding.resolve().getTotalWidth()), Dim.px(imageBinding.resolve().getTotalHeight()));
    }
    public Renderable(AbstractTextureBinding imageBinding, AbstractDimension width, AbstractDimension height){
        this(imageBinding, Pos.px(0), Pos.px(0), width, height);
    }
    public Renderable(AbstractTextureBinding imageBinding, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(xPos, yPos, width, height);

        this.image = imageBinding;
        this.renderColor = Color.WHITE.cpy();

        this.renderDimensionsPerc = new Vector2(1.f, 1.f);

        setPassthrough(false);
    }

    public Renderable(RenderableData data){
        super(data);

        this.image = data.texture.getValue();

        this.preserveAspectRatio = data.preserveAspectRatio.getValue();
        this.noUpsize = data.noUpsize.getValue();

        this.hueShiftAmount = data.hueShiftAmount.getValue();
        this.renderColor = data.renderColor.getColorValue();

        this.renderOrientation = data.renderOrientation.getValue();

        this.renderDimensionsPerc = new Vector2(data.renderDimensionsPerc.getValue().get(0).getValue(), data.renderDimensionsPerc.getValue().get(1).getValue());
        this.renderDimensionsOrientation = data.renderDimensionsOrientation.getValue();

        this.renderOffset = new Vector2(data.positionOffset.getValue().get(0).getValue(), data.positionOffset.getValue().get(1).getValue());
        this.renderScaleOffset = new Vector2(data.renderScaleOffset.getValue().get(0).getValue(), data.renderScaleOffset.getValue().get(1).getValue());
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void renderSelf(SpriteBatch sb) {
        sb.setColor(getColorForRender());

        //Hue shift
        if(hueShiftAmount > 0){
            ShaderManager.pushShader(sb, "hueShift");
            sb.getShader().setUniformf("u_hueShift", (hueShiftAmount / 255f) * 2 * 3.14f);
        }

        NinePatch texture = getTextureForRender();
        if(texture != null){
            NinePatch ninePatchToRender = new NinePatch(texture);
            int textureWidth = ninePatchToRender.getTexture().getWidth();
            int textureHeight = ninePatchToRender.getTexture().getHeight();

            float renderPosX = getWorldPositionX() * Settings.xScale;
            float renderPosY = getWorldPositionY() * Settings.yScale;
            float renderWidth = getWidth() * Settings.xScale;
            float renderHeight = getHeight() * Settings.yScale;

            float rescaleX = 1.0f;
            float rescaleY = 1.0f;
            if(textureWidth > renderWidth){
                rescaleX = renderWidth / textureWidth;
            }
            if(textureHeight > renderHeight){
                rescaleY = renderHeight / textureHeight;
            }
            float min = Math.min(rescaleX, rescaleY);

            ninePatchToRender.scale(min, min);

            // Preserve aspect ratio
            if (isPreservingAspectRatio()) {
                float aspectRatio = (float) textureWidth / (float) textureHeight;
                float containerAspectRatio = renderWidth / renderHeight;

                if (aspectRatio > containerAspectRatio) {
                    renderWidth = renderHeight * aspectRatio;
                } else if (aspectRatio < containerAspectRatio) {
                    renderHeight = renderWidth / aspectRatio;
                }

                if (renderWidth > getWidth() * Settings.xScale) {
                    renderWidth = getWidth() * Settings.xScale;
                    renderHeight = renderWidth / aspectRatio;
                }
                if (renderHeight > getHeight() * Settings.yScale) {
                    renderHeight = getHeight() * Settings.yScale;
                    renderWidth = renderHeight * aspectRatio;
                }
            }

            // No Upscale
            if (isNoUpsize()) {
                if (renderWidth > textureWidth * getScaleX()) {
                    renderWidth = textureWidth * getScaleX();
                }
                if (renderHeight > textureHeight * getScaleY()) {
                    renderHeight = textureHeight * getScaleY();
                }
            }

            renderWidth *= renderScaleOffset.x;
            renderHeight *= renderScaleOffset.y;

            // Apply render orientation
            if (renderOrientation.horizontalAlignment == Alignment.HorizontalAlignment.CENTER) {
                renderPosX = renderPosX + (getWidth() * Settings.xScale - renderWidth) * 0.5f;
            } else if (renderOrientation.horizontalAlignment == Alignment.HorizontalAlignment.RIGHT) {
                renderPosX = renderPosX + getWidth() * Settings.xScale - renderWidth;
            }

            if (renderOrientation.verticalAlignment == Alignment.VerticalAlignment.CENTER) {
                renderPosY = renderPosY + (getHeight() * Settings.yScale - renderHeight) * 0.5f;
            } else if (renderOrientation.verticalAlignment == Alignment.VerticalAlignment.TOP) {
                renderPosY = renderPosY + getHeight() * Settings.yScale - renderHeight;
            }

            renderPosX += renderOffset.x;
            if(renderOrientation.verticalAlignment != Alignment.VerticalAlignment.TOP) renderPosY += renderOffset.y;
            else renderPosY -= renderOffset.y;

            // Apply render dimension percentages
            float noRenderAmountX = (renderWidth * (1 - renderDimensionsPerc.x));
            float noRenderAmountY = (renderHeight * (1 - renderDimensionsPerc.y));

            // Apply render dimension orientation
            float clipPosAddX = 0;
            float clipPosAddY = 0;
            float clipWidth = renderWidth;
            float clipHeight = renderHeight;

            if(renderDimensionsOrientation.horizontalAlignment == Alignment.HorizontalAlignment.LEFT){
                clipWidth = renderWidth - noRenderAmountX;
            }
            else if (renderDimensionsOrientation.horizontalAlignment == Alignment.HorizontalAlignment.CENTER) {
                clipPosAddX = noRenderAmountX / 2;
                clipWidth = renderWidth - noRenderAmountX;
            } else if (renderDimensionsOrientation.horizontalAlignment == Alignment.HorizontalAlignment.RIGHT) {
                clipPosAddX = noRenderAmountX;
                clipWidth = renderWidth - noRenderAmountX;
            }

            if(renderDimensionsOrientation.verticalAlignment == Alignment.VerticalAlignment.BOTTOM){
                clipHeight = renderHeight - noRenderAmountY;
            }
            else if (renderDimensionsOrientation.verticalAlignment == Alignment.VerticalAlignment.CENTER) {
                clipPosAddY = noRenderAmountY / 2;
                clipHeight = renderHeight - noRenderAmountY;
            } else if (renderDimensionsOrientation.verticalAlignment == Alignment.VerticalAlignment.TOP) {
                clipPosAddY = noRenderAmountY;
                clipHeight = renderHeight - noRenderAmountY;
            }

            if(clipPosAddX != 0 || clipPosAddY != 0 || clipWidth != renderWidth || clipHeight != renderHeight){
                // Adjust scissor stack for partial rendering
                Rectangle scissors = new Rectangle();
                Rectangle clipBounds = new Rectangle(renderPosX + clipPosAddX, renderPosY + clipPosAddY, clipWidth, clipHeight);
                ScissorStack.calculateScissors(getCamera(), sb.getTransformMatrix(), clipBounds, scissors);
                if (ScissorStack.pushScissors(scissors)) {
                    renderCall(sb, ninePatchToRender, renderPosX, renderPosY, renderWidth, renderHeight);
                    ScissorStack.popScissors();
                }
            }
            else{
                renderCall(sb, ninePatchToRender, renderPosX, renderPosY, renderWidth, renderHeight);
            }

            sb.flush(); // Flush the SpriteBatch
        }

        if(hueShiftAmount > 0){
            ShaderManager.popShader(sb);
        }

        super.renderSelf(sb);
    }

    protected void renderCall(SpriteBatch sb, NinePatch ninePatchToRender, float renderPosX, float renderPosY, float renderWidth, float renderHeight){
        ninePatchToRender.draw(sb, renderPosX, renderPosY, renderWidth, renderHeight);
        sb.flush();
    }

    //endregion

    //region Image

    public void setTexture(Texture tex){
        setTexture(Tex.stat(tex));
    }
    public void setTexture(TextureRegion tex){
        setTexture(Tex.stat(tex));
    }
    public void setTexture(NinePatch tex){
        setTexture(Tex.stat(tex));
    }
    public void setTexture(Class<?> resourceClazz, String fieldName){
        setTexture(Tex.resource(resourceClazz, fieldName));
    }

    public void setTexture(AbstractTextureBinding image){
        this.image = image;
    }

    protected NinePatch getTextureForRender(){
        return image.resolve();
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

    //region Render Orientation

    public Renderable setRenderOrientation(Alignment orientation){
        renderOrientation = orientation;
        return this;
    }

    public Alignment getRenderOrientation(){
        return renderOrientation;
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

    //region Render Dimension Orientation

    public Renderable setRenderDimensionOrientation(Alignment orientation){
        renderDimensionsOrientation = orientation;
        return this;
    }

    public Alignment getRenderDimensionOrientation(){
        return renderDimensionsOrientation;
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

    public Renderable setNoUpsize(boolean noUpsize){
        this.noUpsize = noUpsize;
        return this;
    }

    public boolean isNoUpsize(){
        return noUpsize;
    }

    //endregion

    //region Scale Offsets

    public Renderable setRenderOffset(Vector2 offset){
        renderOffset = offset;
        return this;
    }

    public Vector2 getRenderOffset(){
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

    //region Hue Shift

    public Renderable setHueShiftAmount(int amount){
        hueShiftAmount = amount;
        return this;
    }

    public int getHueShiftAmount(){
        return hueShiftAmount;
    }

    //endregion

    //endregion

    public static class RenderableData extends UIElement.UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public TextureBindingProperty texture = new TextureBindingProperty(new TextureResourceBinding(UICommonResources.class, "white_pixel"))
                .setName("Image")
                .setDescription("Image to render as this element.")
                .setCategory("Render");

        public BooleanProperty preserveAspectRatio = new BooleanProperty(false)
                .setName("Preserve Aspect Ratio")
                .setDescription("Preserve the aspect ratio of the image when rendering.")
                .setCategory("Render");

        public BooleanProperty noUpsize = new BooleanProperty(false)
                .setName("No Upsize")
                .setDescription("If the image is smaller than the render dimensions, do not resize it to fit the container.")
                .setCategory("Render");

        public IntegerProperty hueShiftAmount = new IntegerProperty(0)
                .setName("Hue Shift Amount")
                .setDescription("Amount to shift the hue of the image. 0 = no shift, 255 = full shift.")
                .setCategory("Render")
                .setMinimumValue(0).setMaximumValue(255);

        public ColorProperty renderColor = new ColorProperty(Color.WHITE.cpy())
                .setName("Render Color")
                .setDescription("Color to render the image with. Not the same as performing a hue shift of the image.")
                .setCategory("Render");

        public AlignmentProperty renderOrientation = new AlignmentProperty(new Alignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER))
                .setName("Render Orientation")
                .setDescription("How to render the image within the element.")
                .setCategory("Render");

        public PropertyArray<Float> positionOffset = new PropertyArray<>(
                new ArrayList<FloatProperty>(){{
                    add(new FloatProperty(0.0f).setName("X"));
                    add(new FloatProperty(0.0f).setName("Y"));
                }}
        )
                .setName("Position Offset")
                .setDescription("Offset for the render position of the rendered image.")
                .setCategory("Render")
                .setValueCountOverride(2);

        public PropertyArray<Float> renderDimensionsPerc = new PropertyArray<>(
                new ArrayList<FloatProperty>(){{
                    add(new FloatProperty(0.0f).setName("W").setMinimumValue(0.0f).setMaximumValue(0.0f));
                    add(new FloatProperty(0.0f).setName("H").setMinimumValue(0.0f).setMaximumValue(0.0f));
                }}
        )
                .setName("Render Dimensions %")
                .setDescription("Percentage of the image to render. 1 = 100% of the image, 0.5 = 50% of the image.")
                .setCategory("Render")
                .setValueCountOverride(2);

        public AlignmentProperty renderDimensionsOrientation = new AlignmentProperty(new Alignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.BOTTOM))
                .setName("Render Dimension Orientation")
                .setDescription("Point from which to render the image. Affected by the render dimensions %.")
                .setCategory("Render");

        public PropertyArray<Float> renderScaleOffset = new PropertyArray<>(
                new ArrayList<FloatProperty>(){{
                    add(new FloatProperty(1f).setName("W"));
                    add(new FloatProperty(1f).setName("H"));
                }}
        )
                .setName("Render Scale")
                .setDescription("Offset for the scale of the rendered image.")
                .setCategory("Render");

        @Override
        public UIElement makeUIElement_internal() {
            return new Renderable(this);
        }
    }
}
