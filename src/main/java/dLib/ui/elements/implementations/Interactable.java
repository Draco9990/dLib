package dLib.ui.elements.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.modcompat.ModManager;
import dLib.ui.elements.UIElement;
import dLib.ui.screens.ScreenManager;
import dLib.util.GlobalEvents;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;
import dLib.util.bindings.texture.TextureNullBinding;
import sayTheSpire.Output;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Interactable extends Hoverable{
    //region Variables

    public Texture hoveredTexture;
    public Texture disabledTexture;

    private Color hoveredColor = Color.BLACK;
    private float hoveredColorMultiplier = 0.25f;

    private Color disabledColor = Color.WHITE;
    private float disabledColorMultiplier = 0.25f;

    private boolean isPassthrough = false;

    private String onHoverSoundKey;
    private String onTriggerSoundKey;
    private String onHoldSoundKey;

    private ArrayList<Runnable> onLeftClickConsumers = new ArrayList<>();
    private ArrayList<Consumer<Float>> onLeftClickHeldConsumers = new ArrayList<>();
    private ArrayList<Runnable> onLeftClickReleaseConsumers = new ArrayList<>();

    private ArrayList<Runnable> onRightClickConsumers = new ArrayList<>();
    private ArrayList<Consumer<Float>> onRightClickHeldConsumers = new ArrayList<>();
    private ArrayList<Runnable> onRightClickReleaseConsumers = new ArrayList<>();

    private ArrayList<Runnable> onSelectedConsumers = new ArrayList<>(); //TODO MOVE TO UIELEMENT
    private ArrayList<Runnable> onUnselectedConsumers = new ArrayList<>(); // TODO MOVE TO UIELEMENt

    protected String onSelectLine; // Say the Spire mod compatibility //TODO MOve to uiELement
    protected String onTriggeredLine; // Say the Spire mod compatibility

    //Temps

    private float totalLeftClickDuration;
    private float totalRightClickDuration;

    private boolean holdingLeft;
    private boolean holdingRight;

    //endregion

    //region Constructors

    public Interactable(Texture image) {
        super(image);
        initialize();
    }
    public Interactable(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
        initialize();
    }
    public Interactable(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
        initialize();
    }

    public Interactable(InteractableData data){
        super(data);

        this.hoveredTexture = data.hoveredTexture.getBoundTexture();
        this.disabledTexture = data.disabledTexture.getBoundTexture();

        if(data.onLeftClick != null) addOnLeftClickConsumer(() -> data.onLeftClick.executeBinding(ScreenManager.getCurrentScreen()));
        if(data.onLeftClickHeld != null) addOnLeftClickHeldConsumer(deltaTime -> data.onLeftClickHeld.executeBinding(ScreenManager.getCurrentScreen(), deltaTime));
        if(data.onLeftClickRelease != null) addOnLeftClickReleaseConsumer(() -> data.onLeftClickRelease.executeBinding(ScreenManager.getCurrentScreen()));

        if(data.onRightClick != null) addOnRightClickConsumer(() -> data.onRightClick.executeBinding(ScreenManager.getCurrentScreen()));
        if(data.onRightClickHeld != null) addOnRightClickHeldConsumer(deltaTime -> data.onRightClickHeld.executeBinding(ScreenManager.getCurrentScreen(), deltaTime));
        if(data.onRightClickRelease != null) addOnRightClickReleaseConsumer(() -> data.onRightClickRelease.executeBinding(ScreenManager.getCurrentScreen()));

        this.isPassthrough = data.isPassthrough;
    }

    private void initialize(){
        this.onHoverSoundKey = "UI_HOVER";
        this.onTriggerSoundKey = "UI_CLICK_1";

        GlobalEvents.subscribe(Events.PreLeftClickEvent.class, (event) -> {
            if(event.source != this && isSelected()){
                deselect(); //TODO RF Move this to UIElement and deselet if child is not source
            }
        });
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void updateSelf() {
        super.updateSelf();

        if(isEnabled()){
            if(isHovered()){
                if(InputHelper.justClickedLeft){
                    clickLeft();
                    if(isPassthrough) InputHelper.justClickedLeft = false;
                }
                if(InputHelper.justClickedRight){
                    clickRight();
                    if(isPassthrough) InputHelper.justClickedRight = false;
                }

            }

            if(InputHelper.justReleasedClickLeft){
                onLeftClickRelease();
            }
            if(InputHelper.justReleasedClickRight){
                onRightButtonRelease();
            }

            if(holdingLeft){
                totalLeftClickDuration += Gdx.graphics.getDeltaTime();
                onLeftClickHeld(totalLeftClickDuration);
            }

            if(holdingRight){
                totalRightClickDuration += Gdx.graphics.getDeltaTime();
                onRightClickHeld(totalRightClickDuration);
            }
        }
    }

    //endregion

    //region Render Texture & Color

    @Override
    protected Texture getTextureForRender() {
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
                return colorToRender;
            }
        }
        else if(isHovered()){
            if(hoveredTexture == null){
                Color colorToRender = getHoveredColor().cpy();
                if(hoveredColorMultiplier != 1.0f){
                    colorToRender = colorToRender.lerp(super.getColorForRender(), 1 - hoveredColorMultiplier);
                }
                return colorToRender;
            }
        }
        return super.getColorForRender();
    }

    //endregion

    //region Triggers

    //region Left Click

    public void trigger(){ clickLeft(); }

    public void clickLeft(){
        onLeftClick();
    }

    protected void onLeftClick(){
        GlobalEvents.sendMessage(new Events.PreLeftClickEvent(this));

        totalLeftClickDuration = 0.f;
        holdingLeft = true;

        if(onTriggerSoundKey != null){
            CardCrawlGame.sound.playA(onTriggerSoundKey, -0.1F);
        }
        if(getOnTriggerLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnTriggerLine(), true);
            }
        }

        select(); //TODO RF Select parents as well

        for(Runnable consumer : onLeftClickConsumers) consumer.run();
    }
    protected void onLeftClickHeld(float totalDuration){
        if(onHoldSoundKey != null){
            CardCrawlGame.sound.playA(onHoldSoundKey, -0.1F);
        }

        for(Consumer<Float> consumer : onLeftClickHeldConsumers) consumer.accept(totalDuration);
    }
    protected void onLeftClickRelease(){
        holdingLeft = false;

        for(Runnable consumer : onLeftClickReleaseConsumers) consumer.run();
    }

    public Interactable addOnLeftClickConsumer(Runnable consumer){
        onLeftClickConsumers.add(consumer);
        return this;
    }
    public Interactable addOnLeftClickHeldConsumer(Consumer<Float> consumer){
        onLeftClickHeldConsumers.add(consumer);
        return this;
    }
    public Interactable addOnLeftClickReleaseConsumer(Runnable consumer){
        onLeftClickReleaseConsumers.add(consumer);
        return this;
    }

    //endregion

    //region Right Click

    public void clickRight(){
        onRightClick();
    }

    protected void onRightClick(){
        totalRightClickDuration = 0.f;
        holdingRight = true;

        if(onTriggerSoundKey != null){
            CardCrawlGame.sound.playA(onTriggerSoundKey, -0.1F);
        }
        if(getOnTriggerLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnTriggerLine(), true);
            }
        }

        for(Runnable consumer : onRightClickConsumers) consumer.run();
    }
    protected void onRightClickHeld(float totalDuration){
        for(Consumer<Float> consumer : onRightClickHeldConsumers) consumer.accept(totalDuration);
    }
    protected void onRightButtonRelease(){
        holdingRight = false;

        for(Runnable consumer : onRightClickReleaseConsumers) consumer.run();
    }

    public Interactable addOnRightClickConsumer(Runnable consumer){
        onRightClickConsumers.add(consumer);
        return this;
    }
    public Interactable addOnRightClickHeldConsumer(Consumer<Float> consumer){
        onRightClickHeldConsumers.add(consumer);
        return this;
    }
    public Interactable addOnRightClickReleaseConsumer(Runnable consumer){
        onRightClickReleaseConsumers.add(consumer);
        return this;
    }

    //endregion

    public Interactable setPassthrough(boolean newValue){
        isPassthrough = newValue;
        return this;
    }

    //endregion

    //region Trigger Sound

    //TODO ADD SPECIFIC FOR LFET AND RIGHT CLICK
    public Interactable setOnTriggerSoundKey(String key){
        onTriggerSoundKey = key;
        return this;
    }
    public Interactable removeOnTriggerSoundKey(){
        onTriggerSoundKey = null;
        return this;
    }

    public Interactable setOnHoldSoundKey(String key){
        onHoldSoundKey = key;
        return this;
    }
    public Interactable removeOnHoldSoundKey(){
        onHoldSoundKey = null;
        return this;
    }

    public Interactable setOnTriggerLine(String newLine) {
        this.onTriggeredLine = newLine;
        return this;
    }
    public String getOnTriggerLine(){
        return onTriggeredLine;
    }

    //endregion

    //region Hovered

    //TODO MOVE TO HVOERABLE
    @Override
    protected void onHovered() {
        super.onHovered();
        if(onHoverSoundKey != null){
            CardCrawlGame.sound.playA(onHoverSoundKey, -0.1F);
        }
    }

    public Interactable setHoveredTexture(Texture hoveredTexture){
        this.hoveredTexture = hoveredTexture;
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
        this.disabledTexture = disabledTexture;
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

    //endregion

    /** Events */
    public static class Events{
        public static class PreLeftClickEvent {
            public Interactable source;

            public PreLeftClickEvent(Interactable source){
                this.source = source;
            }
        }
    }

    public static class InteractableData extends Hoverable.HoverableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public TextureBinding hoveredTexture = new TextureNullBinding();
        public TextureBinding disabledTexture = new TextureNullBinding();

        //TODO HOVEREDCOLOR & mult
        //TODO DISABLEDCOLOR & mult

        public boolean isPassthrough = false;

        //TODO ON HOVER KEY
        //TODO ON TRIGGER KEY
        //TODO ON HOLD KEY

        public MethodBinding onLeftClick = new NoneMethodBinding();
        public MethodBinding onLeftClickHeld = new NoneMethodBinding();
        public MethodBinding onLeftClickRelease = new NoneMethodBinding();

        public MethodBinding onRightClick = new NoneMethodBinding();
        public MethodBinding onRightClickHeld = new NoneMethodBinding();
        public MethodBinding onRightClickRelease = new NoneMethodBinding();

        @Override
        public Interactable makeUIElement() {
            return new Interactable(this);
        }
    }
}
