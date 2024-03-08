package dLib.ui.elements.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.modcompat.ModManager;
import dLib.ui.data.implementations.InteractableData;
import dLib.ui.screens.ScreenManager;
import dLib.util.GlobalEvents;
import sayTheSpire.Output;

import java.util.ArrayList;
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

    private ArrayList<Runnable> onLeftClickConsumers = new ArrayList<>();
    private ArrayList<Consumer<Float>> onLeftClickHeldConsumers = new ArrayList<>();
    private ArrayList<Runnable> onLeftClickReleaseConsumers = new ArrayList<>();

    private ArrayList<Runnable> onRightClickConsumers = new ArrayList<>();
    private ArrayList<Consumer<Float>> onRightClickHeldConsumers = new ArrayList<>();
    private ArrayList<Runnable> onRightClickReleaseConsumers = new ArrayList<>();

    private ArrayList<Runnable> onSelectedConsumers = new ArrayList<>();
    private ArrayList<Runnable> onUnselectedConsumers = new ArrayList<>();

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

        if(data.onLeftClick != null) addOnLeftClickConsumer(() -> data.onLeftClick.executeBinding(ScreenManager.getCurrentScreen()));
        if(data.onLeftClickHeld != null) addOnLeftClickHeldConsumer(deltaTime -> data.onLeftClickHeld.executeBinding(ScreenManager.getCurrentScreen(), deltaTime));
        if(data.onLeftClickRelease != null) addOnLeftClickReleaseConsumer(() -> data.onLeftClickRelease.executeBinding(ScreenManager.getCurrentScreen()));

        if(data.onRightClick != null) addOnRightClickConsumer(() -> data.onRightClick.executeBinding(ScreenManager.getCurrentScreen()));
        if(data.onRightClickHeld != null) addOnRightClickHeldConsumer(deltaTime -> data.onRightClickHeld.executeBinding(ScreenManager.getCurrentScreen(), deltaTime));
        if(data.onRightClickRelease != null) addOnRightClickReleaseConsumer(() -> data.onRightClickRelease.executeBinding(ScreenManager.getCurrentScreen()));
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
        public static class PreLeftClickEvent {
            public Interactable source;

            public PreLeftClickEvent(Interactable source){
                this.source = source;
            }
        }
    }
}
