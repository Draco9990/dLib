package dLib.ui.elements.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.modcompat.ModManager;
import sayTheSpire.Output;

public class Interactable extends Hoverable{
    /** Variables */
    public Texture hoveredTexture;
    public Texture disabledTexture;

    private boolean consumeClickEvent = true;
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

    private void initialize(){
        this.onHoverSoundKey = "UI_HOVER";
        this.onTriggerSoundKey = "UI_CLICK_1";
    }

    /** Builder methods */

    public Interactable setConsumeClickEvent(boolean newValue){
        consumeClickEvent = newValue;
        return this;
    }
    public Interactable setHoveredTexture(Texture hoveredTexture){
        this.hoveredTexture = hoveredTexture;
        return this;
    }
    public Interactable setDisabledTexture(Texture disabledTexture){
        this.disabledTexture = disabledTexture;
        return this;
    }

    public Interactable setOnHoverSoundKey(String key){
        onHoverSoundKey = key;
        return this;
    }
    public Interactable removeOnHoverSoundKey(){
        onHoverSoundKey = null;
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


    public Interactable setOnSelectLine(String newLine){
        this.onSelectLine = newLine;
        return this;
    }
    public String getOnSelectLine(){
        return onSelectLine;
    }
    public Interactable setOnTriggerLine(String newLine) {
        this.onTriggeredLine = newLine;
        return this;
    }
    public String getOnTriggerLine(){
        return onTriggeredLine;
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
                    if(consumeClickEvent) InputHelper.justClickedLeft = false;
                }
                else if(InputHelper.justReleasedClickLeft){
                    onLeftClickRelease();
                }

                if(InputHelper.justClickedRight){
                    clickRight();
                    if(consumeClickEvent) InputHelper.justClickedRight = false;
                }
                else if(InputHelper.justReleasedClickRight){
                    onRightButtonRelease();
                }
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
                return Color.LIGHT_GRAY;
            }
        }
        else if(isHovered()){
            if(hoveredTexture == null){
                return Color.LIGHT_GRAY;
            }
        }
        return super.getColorForRender();
    }

    /** Misc methods */

    @Override
    protected void onHovered() {
        super.onHovered();
        if(onHoverSoundKey != null){
            CardCrawlGame.sound.playA(onHoverSoundKey, -0.1F);
        }
    }

    public void clickLeft(){
        onLeftClick();
    }
    public void clickRight(){
        onRightClick();
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
    }
    protected void onLeftClickHeld(float totalDuration){
        if(onHoldSoundKey != null){
            CardCrawlGame.sound.playA(onHoldSoundKey, -0.1F);
        }
    }
    protected void onRightClickHeld(float totalDuration){}
    protected void onLeftClickRelease(){
        holdingLeft = false;
    }
    protected void onRightButtonRelease(){
        holdingRight = false;
    }

    public void select(){
        setSelected(true);
    }
    public void deselect(){
        setSelected(false);
    }
    protected void setSelected(boolean selected){
        this.selected = selected;

        if(selected){
            if(ModManager.SayTheSpire.isActive()){
                if(getOnSelectLine() != null){
                    Output.text(getOnSelectLine(), true);
                }
            }
        }
    }
    public boolean isSelected(){
        return selected;
    }
}
