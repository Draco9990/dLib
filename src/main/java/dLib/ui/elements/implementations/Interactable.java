package dLib.ui.elements.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.modcompat.ModManager;
import dLib.ui.data.implementations.InteractableData;
import dLib.util.GlobalEvents;
import dLib.util.TextureManager;
import sayTheSpire.Output;

import java.util.function.Consumer;

public class Interactable extends Hoverable{
    /** Variables */
    public Texture hoveredTexture;
    public Texture disabledTexture;

    private Color hoveredColor = Color.LIGHT_GRAY;
    private Color disabledColor = Color.LIGHT_GRAY;

    private boolean consumeTriggerEvent = true;
    protected boolean selected = false;

    private float totalLeftClickDuration;
    private float totalRightClickDuration;

    private String onHoverSoundKey;
    private String onTriggerSoundKey;
    private String onHoldSoundKey;

    protected String onSelectLine; // Say the Spire mod compatibility
    protected String onTriggeredLine; // Say the Spire mod compatibility

    private boolean holdingLeft;
    private boolean holdingRight;

    private Runnable onLeftClickConsumer;
    private Consumer<Float> onLeftClickHeldConsumer;
    private Runnable onLeftClickReleaseConsumer;

    private Runnable onRightClickConsumer;
    private Consumer<Float> onRightClickHeldConsumer;
    private Runnable onRightClickReleaseConsumer;

    private Runnable onSelectedConsumer;
    private Runnable onUnselectedConsumer;

    /** Constructors */
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

        this.hoveredTexture = TextureManager.getTexture(data.hoveredTexture);
        this.disabledTexture = TextureManager.getTexture(data.disabledTexture);

        this.hoveredColor = Color.valueOf(data.hoveredColor);
        this.disabledColor = Color.valueOf(data.disabledColor);

        this.consumeTriggerEvent = data.consumeTriggerEvent;
    }

    private void initialize(){
        this.onHoverSoundKey = "UI_HOVER";
        this.onTriggerSoundKey = "UI_CLICK_1";

        GlobalEvents.subscribe(Events.OnLeftClickEvent.class, (event) -> {
            if(event.source != this && isSelected()){
                deselect();
            }
        });
    }

    /** Update and render */
    @Override
    public void update() {
        super.update();
        if(!shouldUpdate()) return;

        if(isEnabled()){
            if(isHovered()){
                if(InputHelper.justClickedLeft){
                    clickLeft();
                    if(consumeTriggerEvent) InputHelper.justClickedLeft = false;
                }
                if(InputHelper.justClickedRight){
                    clickRight();
                    if(consumeTriggerEvent) InputHelper.justClickedRight = false;
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

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

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
                return getDisabledColor();
            }
        }
        else if(isHovered()){
            if(hoveredTexture == null){
                return getHoveredColor();
            }
        }
        return super.getColorForRender();
    }

    /** Trigger */
    public void clickLeft(){
        onLeftClick();
    }

    protected void onLeftClick(){
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

        select();

        if(onLeftClickConsumer != null) onLeftClickConsumer.run();
        GlobalEvents.sendMessage(new Events.OnLeftClickEvent(this));
    }
    protected void onLeftClickHeld(float totalDuration){
        if(onHoldSoundKey != null){
            CardCrawlGame.sound.playA(onHoldSoundKey, -0.1F);
        }

        if(onLeftClickHeldConsumer != null) onLeftClickHeldConsumer.accept(totalDuration);
    }
    protected void onLeftClickRelease(){
        holdingLeft = false;

        if(onLeftClickReleaseConsumer != null) onLeftClickReleaseConsumer.run();
    }

    public Interactable setOnLeftClickConsumer(Runnable consumer){
        onLeftClickConsumer = consumer;
        return this;
    }
    public Interactable setOnLeftClickHeldConsumer(Consumer<Float> consumer){
        onLeftClickHeldConsumer = consumer;
        return this;
    }
    public Interactable setOnLeftClickReleaseConsumer(Runnable consumer){
        onLeftClickReleaseConsumer = consumer;
        return this;
    }

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

        if(onRightClickConsumer != null) onRightClickConsumer.run();
    }
    protected void onRightClickHeld(float totalDuration){
        if(onRightClickHeldConsumer != null) onRightClickHeldConsumer.accept(totalDuration);
    }
    protected void onRightButtonRelease(){
        holdingRight = false;

        if(onRightClickReleaseConsumer != null) onRightClickReleaseConsumer.run();
    }

    public Interactable setOnRightClickConsumer(Runnable consumer){
        onRightClickConsumer = consumer;
        return this;
    }
    public Interactable setOnRightClickHeldConsumer(Consumer<Float> consumer){
        onRightClickHeldConsumer = consumer;
        return this;
    }
    public Interactable setOnRightClickReleaseConsumer(Runnable consumer){
        onRightClickReleaseConsumer = consumer;
        return this;
    }

    public Interactable setConsumeTriggerEvent(boolean newValue){
        consumeTriggerEvent = newValue;
        return this;
    }

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
    public Interactable removeOnHoveredSoundKey(){
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

    /** Select */
    public void select(){
        setSelected(true);
    }
    public void deselect(){
        setSelected(false);
    }

    protected void onSelected(){
        if(onSelectedConsumer != null) onSelectedConsumer.run();
    }
    protected void onUnselected(){
        if(onUnselectedConsumer != null) onUnselectedConsumer.run();
    }

    public Interactable setOnSelectedConsumer(Runnable consumer){
        onSelectedConsumer = consumer;
        return this;
    }
    public Interactable setOnUnselectedConsumer(Runnable consumer){
        onUnselectedConsumer = consumer;
        return this;
    }

    protected void setSelected(boolean selected){
        if(selected == this.selected) return;

        this.selected = selected;

        if(selected){
            onSelected();
            if(ModManager.SayTheSpire.isActive()){
                if(getOnSelectLine() != null){
                    Output.text(getOnSelectLine(), true);
                }
            }
        }
        else{
            onUnselected();
        }
    }
    public boolean isSelected(){
        return selected;
    }

    public Interactable setOnSelectLine(String newLine){
        this.onSelectLine = newLine;
        return this;
    }
    public String getOnSelectLine(){
        return onSelectLine;
    }

    /** Hover */
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

    public Interactable setOnHoverSoundKey(String key){
        onHoverSoundKey = key;
        return this;
    }
    public Interactable removeOnHoverSoundKey(){
        onHoverSoundKey = null;
        return this;
    }

    /** Disable */
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

    /** Events */
    public static class Events{
        public static class OnLeftClickEvent{
            public Interactable source;

            public OnLeftClickEvent(Interactable source){
                this.source = source;
            }
        }
    }
}
