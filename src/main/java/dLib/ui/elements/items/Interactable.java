package dLib.ui.elements.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import dLib.properties.objects.ColorProperty;
import dLib.properties.objects.FloatProperty;
import dLib.properties.objects.TextureBindingProperty;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class Interactable extends Renderable{
    //region Variables

    public AbstractTextureBinding hoveredTexture = new TextureNoneBinding();
    public AbstractTextureBinding disabledTexture = new TextureNoneBinding();

    private Color hoveredColor = Color.BLACK;
    private float hoveredColorMultiplier = 0.25f;

    private Color disabledColor = Color.WHITE;
    private float disabledColorMultiplier = 0.25f;

    private String onHoverSoundKey = "UI_HOVER"; //TODO expose
    private String onTriggerSoundKey = "UI_CLICK_1"; //TODO expose
    private String onHoldSoundKey; //TODO expose

    //endregion

    //region Constructors

    public Interactable(AbstractTextureBinding imageBinding) {
        this(imageBinding, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
    }
    public Interactable(AbstractTextureBinding imageBinding, AbstractPosition xPos, AbstractPosition yPos) {
        this(imageBinding, xPos, yPos, Dim.fill(), Dim.fill());
    }
    public Interactable(AbstractTextureBinding imageBinding, AbstractDimension width, AbstractDimension height) {
        this(imageBinding, Pos.px(0), Pos.px(0), width, height);
    }
    public Interactable(AbstractTextureBinding imageBinding, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(imageBinding, xPos, yPos, width, height);

        setControllerSelectable(true);
    }

    public Interactable(InteractableData data){
        super(data);

        this.hoveredTexture = data.hoveredTexture.getValue();
        this.disabledTexture = data.disabledTexture.getValue();

        this.hoveredColor = data.hoveredColor.getColorValue();
        this.hoveredColorMultiplier = data.hoveredColorMultiplier.getValue();

        this.disabledColor = data.disabledColor.getColorValue();
        this.disabledColorMultiplier = data.disabledColorMultiplier.getValue();

        if(data.onLeftClick != null) postLeftClickEvent.subscribeManaged(() -> data.onLeftClick.getValue().executeBinding(this));
        if(data.onLeftClickHeld != null) postLeftClickHeldEvent.subscribeManaged(deltaTime -> data.onLeftClickHeld.getValue().executeBinding(this, deltaTime));
        if(data.onLeftClickRelease != null) postLeftClickReleaseEvent.subscribeManaged(() -> data.onLeftClickRelease.getValue().executeBinding(this));

        if(data.onRightClick != null) postRightClickEvent.subscribeManaged(() -> data.onRightClick.getValue().executeBinding(this));
        if(data.onRightClickHeld != null) postRightClickHeldEvent.subscribeManaged(deltaTime -> data.onRightClickHeld.getValue().executeBinding(this, deltaTime));
        if(data.onRightClickRelease != null) postRightClickReleaseEvent.subscribeManaged(() -> data.onRightClickRelease.getValue().executeBinding(this));
    }

    //endregion

    //region Methods

    //region Update & Render

    //endregion

    //region Render Texture & Color

    @Override
    protected NinePatch getTextureForRender() {
        if(!isEnabled() && disabledTexture.resolve() != null) return disabledTexture.resolve();
        if(isHovered() && hoveredTexture.resolve() != null) return hoveredTexture.resolve();
        return super.getTextureForRender();
    }

    @Override
    protected Color getColorForRender() {
        if(!isEnabled()){
            if(disabledTexture.resolve() == null){
                Color colorToRender = getDisabledColor().cpy();
                if(disabledColorMultiplier != 1.0f){
                    colorToRender = colorToRender.lerp(super.getColorForRender(), 1 - disabledColorMultiplier);
                }

                colorToRender.a *= getRenderColorAlphaMultiplier();

                return colorToRender;
            }
        }
        else if(isHovered()){
            if(hoveredTexture.resolve() == null){
                Color colorToRender = getHoveredColor().cpy();
                if(hoveredColorMultiplier != 1.0f){
                    colorToRender = colorToRender.lerp(super.getColorForRender(), 1 - hoveredColorMultiplier);
                }

                colorToRender.a *= getRenderColorAlphaMultiplier();

                return colorToRender;
            }
        }
        return super.getColorForRender();
    }

    //endregion

    //region Trigger Sound

    //TODO ADD SPECIFIC FOR LFET AND RIGHT CLICK
    public void setOnTriggerSoundKey(String key){
        onTriggerSoundKey = key;
    }
    public void removeOnTriggerSoundKey(){
        onTriggerSoundKey = null;
    }

    public void setOnHoldSoundKey(String key){
        onHoldSoundKey = key;
    }
    public void removeOnHoldSoundKey(){
        onHoldSoundKey = null;
    }

    protected String getOnHoverSoundKey() {
        return onHoverSoundKey;
    }

    //endregion

    //region Hovered

    @Override
    protected void onHovered() {
        super.onHovered();
        if(getOnHoverSoundKey() != null){
            CardCrawlGame.sound.playA(getOnHoverSoundKey(), -0.1F);
        }
    }

    public void setHoveredTexture(AbstractTextureBinding hoveredTexture){
        this.hoveredTexture = hoveredTexture;
    }

    public void setHoveredColor(Color hoveredColor){
        this.hoveredColor = hoveredColor;
    }
    public Color getHoveredColor(){
        return hoveredColor;
    }

    public void setHoveredColorMultiplier(float hoveredColorMultiplier){
        this.hoveredColorMultiplier = hoveredColorMultiplier;
        if(this.hoveredColorMultiplier > 1.0f) this.hoveredColorMultiplier = 1.0f;
    }
    public Float getHoveredColorMultiplier(){
        return hoveredColorMultiplier;
    }

    public void setOnHoverSoundKey(String key){
        onHoverSoundKey = key;
    }
    public void removeOnHoverSoundKey(){
        onHoverSoundKey = null;
    }

    //endregion

    //region Disabled

    //TODO MOVE TO RENDERABLE
    public void setDisabledTexture(AbstractTextureBinding disabledTexture){
        this.disabledTexture = disabledTexture;
    }

    public void setDisabledColor(Color disabledColor){
        this.disabledColor = disabledColor;
    }
    public Color getDisabledColor(){
        return disabledColor;
    }

    public void setDisabledColorMultiplier(float disabledColorMultiplier){
        this.disabledColorMultiplier = disabledColorMultiplier;
        if(this.disabledColorMultiplier > 1.0f) this.disabledColorMultiplier = 1.0f;
    }
    public Float getDisabledColorMultiplier(){
        return disabledColorMultiplier;
    }

    //endregion

    //region Sound

    protected String getOnTriggerSoundKey() {
        return onTriggerSoundKey;
    }

    protected String getOnHoldSoundKey() {
        return onHoldSoundKey;
    }

    @Override
    protected boolean onLeftClick(boolean byProxy) {
        if(getOnTriggerSoundKey() != null){
            CardCrawlGame.sound.playA(getOnTriggerSoundKey(), -0.1F);
        }

        return super.onLeftClick(byProxy);
    }

    @Override
    protected void onLeftClickHeld(float totalDuration) {
        if(getOnHoldSoundKey() != null){
            CardCrawlGame.sound.playA(getOnHoldSoundKey(), -0.1F);
        }

        super.onLeftClickHeld(totalDuration);
    }

    @Override
    protected void onRightClick() {
        if(getOnTriggerSoundKey() != null){
            CardCrawlGame.sound.playA(getOnTriggerSoundKey(), -0.1F);
        }

        super.onRightClick();
    }

    @Override
    protected void onRightClickHeld(float totalDuration) {
        if(getOnHoldSoundKey() != null){
            CardCrawlGame.sound.playA(getOnHoldSoundKey(), -0.1F);
        }

        super.onRightClickHeld(totalDuration);
    }

    //endregion

    //endregion

    public static class InteractableData extends RenderableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public TextureBindingProperty hoveredTexture = new TextureBindingProperty(new TextureNoneBinding())
                .setName("Hovered Texture")
                .setDescription("The texture to display when the interactable is hovered over. If not set, the default texture will be used in combination with the hovered color..")
                .setCategory("Interactions");
        public ColorProperty hoveredColor = new ColorProperty(Color.BLACK)
                .setName("Hovered Color")
                .setDescription("The color to tint the interactable when hovered over. If a hovered texture is set, this color will be ignored.")
                .setCategory("Interactions")
                .addIsPropertyVisibleFunction(stringProperty -> hoveredTexture.getValue() instanceof TextureNoneBinding);
        public FloatProperty hoveredColorMultiplier = new FloatProperty(0.25f)
                .setName("Hovered Color Multiplier")
                .setDescription("The amount to tint the interactable when hovered over. 0.0f is no tint, 1.0f is full tint.")
                .setCategory("Interactions")
                .setMinimumValue(0.0f).setMaximumValue(1.0f)
                .addIsPropertyVisibleFunction(floatProperty -> hoveredTexture.getValue() instanceof TextureNoneBinding);

        public TextureBindingProperty disabledTexture = new TextureBindingProperty(new TextureNoneBinding())
                .setName("Disabled Texture")
                .setDescription("The texture to display when the interactable is disabled. If not set, the default texture will be used in combination with the disabled color.")
                .setCategory("Interactions");
        public ColorProperty disabledColor = new ColorProperty(Color.WHITE)
                .setName("Disabled Color")
                .setDescription("The color to tint the interactable when disabled. If a disabled texture is set, this color will be ignored.")
                .setCategory("Interactions")
                .addIsPropertyVisibleFunction(stringProperty -> disabledTexture.getValue() instanceof TextureNoneBinding);
        public FloatProperty disabledColorMultiplier = new FloatProperty(0.25f)
                .setName("Disabled Color Multiplier")
                .setDescription("The amount to tint the interactable when disabled. 0.0f is no tint, 1.0f is full tint.")
                .setCategory("Interactions")
                .setMinimumValue(0.0f).setMaximumValue(1.0f)
                .addIsPropertyVisibleFunction(floatProperty -> disabledTexture.getValue() instanceof TextureNoneBinding);

        public InteractableData(){
            isControllerSelectable.setValue(true);
        }

        @Override
        public Interactable makeUIElement_internal() {
            return new Interactable(this);
        }
    }
}
