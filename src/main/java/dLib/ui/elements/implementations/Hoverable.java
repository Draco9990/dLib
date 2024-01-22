package dLib.ui.elements.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import dLib.modcompat.ModManager;
import sayTheSpire.Output;

public class Hoverable extends Renderable{
    protected Hitbox hb;

    protected boolean enabled = true;

    private float totalHoverDuration;

    private String onHoverLine; // Say the Spire mod compatibility

    public Hoverable(Texture image) {
        super(image);
        initialize();
    }
    public Hoverable(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
        initialize();
    }
    public Hoverable(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
        initialize();

    }

    private void initialize(){
        hb = new Hitbox(x * Settings.xScale, y * Settings.yScale, width * Settings.xScale, height * Settings.yScale);
    }

    /** Builder methods */

    public Hitbox getHitbox(){
        return hb;
    }

    @Override
    public Hoverable setPositionX(int newPosX) {
        super.setPositionX(newPosX);
        hb.x = newPosX * Settings.xScale;

        return this;
    }

    @Override
    public Hoverable setPositionY(int newPosY) {
        super.setPositionY(newPosY);
        hb.y = newPosY * Settings.yScale;

        return this;
    }

    @Override
    public Hoverable setWidth(int newWidth) {
        super.setWidth(newWidth);
        hb.width = newWidth * Settings.xScale;

        return this;
    }

    @Override
    public Hoverable setHeight(int newHeight) {
        super.setHeight(newHeight);
        hb.height = newHeight * Settings.yScale;

        return this;
    }

    public Hoverable setOnHoverLine(String newLine){
        this.onHoverLine = newLine;
        return this;
    }
    public String getOnHoverLine(){
        return onHoverLine;
    }

    /** Update and render */

    @Override
    public void update() {
        super.update();
        if(!shouldUpdate()) return;

        if(hb != null){
            boolean hbHoveredCache = this.hb.hovered || this.hb.justHovered;
            this.hb.update();

            if(isEnabled()){
                if(this.hb.justHovered) onHovered();
                if(this.hb.hovered){
                    totalHoverDuration += Gdx.graphics.getDeltaTime();
                    onHoverTick(totalHoverDuration);
                }
            }

            if(hbHoveredCache &&
                (!this.hb.hovered && !this.hb.justHovered)){
                onUnhovered();
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if(!shouldRender()) return;

        if(hb != null){
            hb.render(sb);
        }
    }

    /** Misc methods */

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        this.enabled = enabled;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    protected void onHovered(){
        totalHoverDuration = 0.f;

        if(getOnHoverLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnHoverLine(), true);
            }
        }
    }
    protected void onHoverTick(float totalTickDuration){}
    protected void onUnhovered(){
        totalHoverDuration = 0.f;
    }
    public boolean isHovered(){ return hb.hovered || hb.justHovered; }
}
