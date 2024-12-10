package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import dLib.properties.objects.MethodBindingProperty;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureNullBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class Interactable extends Renderable{
    //region Variables

    public TextureRegion hoveredTexture;
    public TextureRegion disabledTexture;

    private Color hoveredColor = Color.BLACK;
    private float hoveredColorMultiplier = 0.25f;

    private Color disabledColor = Color.WHITE;
    private float disabledColorMultiplier = 0.25f;

    private String onHoverSoundKey = "UI_HOVER";
    private String onTriggerSoundKey = "UI_CLICK_1";
    private String onHoldSoundKey;

    //Temps


    //endregion

    //region Constructors

    public Interactable(Texture image) {
        this(image, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
    }
    public Interactable(Texture image, AbstractPosition xPos, AbstractPosition yPos) {
        this(image, xPos, yPos, Dim.fill(), Dim.fill());
    }
    public Interactable(Texture image, AbstractDimension width, AbstractDimension height) {
        this(image, Pos.px(0), Pos.px(0), width, height);
    }
    public Interactable(Texture image, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(image, xPos, yPos, width, height);
    }

    public Interactable(InteractableData data){
        super(data);

        Texture hoveredTextureT = data.hoveredTexture.getBoundTexture();
        Texture disabledTextureT = data.disabledTexture.getBoundTexture();
        if(hoveredTextureT != null) this.hoveredTexture = new TextureRegion(hoveredTextureT);
        if(disabledTextureT != null) this.disabledTexture = new TextureRegion(disabledTextureT);

        this.hoveredColor = Color.valueOf(data.hoveredColor);
        this.hoveredColorMultiplier = data.hoveredColorMultiplier;

        this.disabledColor = Color.valueOf(data.disabledColor);
        this.disabledColorMultiplier = data.disabledColorMultiplier;

        if(data.onLeftClick != null) addOnLeftClickEvent(() -> data.onLeftClick.getValue().executeBinding(getTopParent()));
        if(data.onLeftClickHeld != null) addOnLeftClickHeldEvent(deltaTime -> data.onLeftClickHeld.getValue().executeBinding(getTopParent(), deltaTime));
        if(data.onLeftClickRelease != null) addOnLeftClickReleaseEvent(() -> data.onLeftClickRelease.getValue().executeBinding(getTopParent()));

        if(data.onRightClick != null) addOnRightClickEvent(() -> data.onRightClick.getValue().executeBinding(getTopParent()));
        if(data.onRightClickHeld != null) addOnRightClickHeldEvent(deltaTime -> data.onRightClickHeld.getValue().executeBinding(getTopParent(), deltaTime));
        if(data.onRightClickRelease != null) addOnRightClickReleaseEvent(() -> data.onRightClickRelease.getValue().executeBinding(getTopParent()));
    }

    //endregion

    //region Methods

    //region Update & Render

    //endregion

    //region Render Texture & Color

    @Override
    protected TextureRegion getTextureForRender() {
        if(!isEnabled() && disabledTexture != null) return disabledTexture;
        if(isHovered() && hoveredTexture != null) return hoveredTexture;
        return super.getTextureForRender();
    }

    @Override
    protected Color getColorForRender() {
        if(!isEnabled()){
            if(disabledTexture == null){
                Color colorToRender = getDisabledColor().cpy();
                if(disabledColorMultiplier != 1.0f){
                    colorToRender = colorToRender.lerp(super.getColorForRender(), 1 - disabledColorMultiplier);
                }

                colorToRender.a *= getRenderColorAlphaMultiplier();

                return colorToRender;
            }
        }
        else if(isHovered()){
            if(hoveredTexture == null){
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

    //endregion

    //region Hovered

    @Override
    protected void onHovered() {
        super.onHovered();
        if(onHoverSoundKey != null){
            CardCrawlGame.sound.playA(onHoverSoundKey, -0.1F);
        }
    }

    public Interactable setHoveredTexture(Texture hoveredTexture){
        if(hoveredTexture == null) this.hoveredTexture = null;
        else this.hoveredTexture = new TextureRegion(hoveredTexture);
        return this;
    }

    public Interactable setHoveredColor(Color hoveredColor){
        this.hoveredColor = hoveredColor;
        return this;
    }
    public Color getHoveredColor(){
        return hoveredColor;
    }

    public Interactable setHoveredColorMultiplier(float hoveredColorMultiplier){
        this.hoveredColorMultiplier = hoveredColorMultiplier;
        if(this.hoveredColorMultiplier > 1.0f) this.hoveredColorMultiplier = 1.0f;
        return this;
    }
    public Float getHoveredColorMultiplier(){
        return hoveredColorMultiplier;
    }

    public Interactable setOnHoverSoundKey(String key){
        onHoverSoundKey = key;
        return this;
    }
    public Interactable removeOnHoverSoundKey(){
        onHoverSoundKey = null;
        return this;
    }

    //endregion

    //region Disabled

    //TODO MOVE TO RENDERABLE
    public Interactable setDisabledTexture(Texture disabledTexture){
        if(disabledTexture == null) this.disabledTexture = null;
        else this.disabledTexture = new TextureRegion(disabledTexture);
        return this;
    }

    public Interactable setDisabledColor(Color disabledColor){
        this.disabledColor = disabledColor;
        return this;
    }
    public Color getDisabledColor(){
        return disabledColor;
    }

    public Interactable setDisabledColorMultiplier(float disabledColorMultiplier){
        this.disabledColorMultiplier = disabledColorMultiplier;
        if(this.disabledColorMultiplier > 1.0f) this.disabledColorMultiplier = 1.0f;
        return this;
    }
    public Float getDisabledColorMultiplier(){
        return disabledColorMultiplier;
    }

    //endregion

    //region Sound

    @Override
    protected void onLeftClick() {
        if(onTriggerSoundKey != null){
            CardCrawlGame.sound.playA(onTriggerSoundKey, -0.1F);
        }

        super.onLeftClick();
    }

    @Override
    protected void onLeftClickHeld(float totalDuration) {
        if(onHoldSoundKey != null){
            CardCrawlGame.sound.playA(onHoldSoundKey, -0.1F);
        }

        super.onLeftClickHeld(totalDuration);
    }

    @Override
    protected void onRightClick() {
        if(onTriggerSoundKey != null){
            CardCrawlGame.sound.playA(onTriggerSoundKey, -0.1F);
        }

        super.onRightClick();
    }

    @Override
    protected void onRightClickHeld(float totalDuration) {
        if(onHoldSoundKey != null){
            CardCrawlGame.sound.playA(onHoldSoundKey, -0.1F);
        }

        super.onRightClickHeld(totalDuration);
    }

    //endregion

    //endregion

    public static class InteractableData extends RenderableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public TextureBinding hoveredTexture = new TextureNullBinding();
        public TextureBinding disabledTexture = new TextureNullBinding();

        public String hoveredColor = Color.BLACK.toString();
        public float hoveredColorMultiplier = 0.25f;

        public String disabledColor = Color.WHITE.toString();
        public float disabledColorMultiplier = 0.25f;

        //TODO ON HOVER KEY
        //TODO ON TRIGGER KEY
        //TODO ON HOLD KEY

        public MethodBindingProperty onLeftClick = new MethodBindingProperty(new NoneMethodBinding()).setName("On Left Click");
        public MethodBindingProperty onLeftClickHeld = new MethodBindingProperty(new NoneMethodBinding()).setName("On Left Click Held").addDNCParameter("holdDuration", float.class);
        public MethodBindingProperty onLeftClickRelease = new MethodBindingProperty(new NoneMethodBinding()).setName("On Left Click Release");

        public MethodBindingProperty onRightClick = new MethodBindingProperty(new NoneMethodBinding()).setName("On Right Click");
        public MethodBindingProperty onRightClickHeld = new MethodBindingProperty(new NoneMethodBinding()).setName("On Right Click Held").addDNCParameter("holdDuration", float.class);
        public MethodBindingProperty onRightClickRelease = new MethodBindingProperty(new NoneMethodBinding()).setName("On Right Click Release");

        public InteractableData(){
            //TODO handle serialization
            id.addOnValueChangedListener((s, s2) -> {
                onLeftClick.setDNCMethodName(s2 + "_onLeftClick");
                onLeftClickHeld.setDNCMethodName(s2 + "_onLeftClickHeld");
                onLeftClickRelease.setDNCMethodName(s2 + "_onLeftClickRelease");

                onRightClick.setDNCMethodName(s2 + "_onRightClick");
                onRightClickHeld.setDNCMethodName(s2 + "_onRightClickHeld");
                onRightClickRelease.setDNCMethodName(s2 + "_onRightClickRelease");
            });
        }

        @Override
        public Interactable makeUIElement() {
            return new Interactable(this);
        }
    }
}
