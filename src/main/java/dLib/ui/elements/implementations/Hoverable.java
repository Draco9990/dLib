package dLib.ui.elements.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import dLib.modcompat.ModManager;
import dLib.ui.data.implementations.HoverableData;
import sayTheSpire.Output;

import java.util.function.Consumer;

public class Hoverable extends Renderable{
    /** Variables */
    protected Hitbox hb;

    protected boolean enabled = true;

    private float totalHoverDuration;

    private String onHoverLine; // Say the Spire mod compatibility

    private Runnable onHoveredConsumer;
    private Consumer<Float> onHoverTickConsumer;
    private Runnable onUnhoveredConsumer;

    /** Constructors */
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

    public Hoverable(HoverableData data){
        super(data);
        initialize();
    }

    private void initialize(){
        hb = new Hitbox(x * Settings.xScale, y * Settings.yScale, width * Settings.xScale, height * Settings.yScale);
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

    /** Hover */
    protected void onHovered(){
        totalHoverDuration = 0.f;

        if(getOnHoverLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnHoverLine(), true);
            }
        }
        if(onHoveredConsumer != null){
            onHoveredConsumer.run();
        }
    }
    protected void onHoverTick(float totalTickDuration){
        if(onHoverTickConsumer != null){
            onHoverTickConsumer.accept(totalTickDuration);
        }
    }
    protected void onUnhovered(){
        totalHoverDuration = 0.f;

        if(onUnhoveredConsumer != null){
            onUnhoveredConsumer.run();
        }
    }

    public boolean isHovered(){ return hb.hovered || hb.justHovered; }

    public Hoverable setOnHoveredConsumer(Runnable consumer){
        onHoveredConsumer = consumer;
        return this;
    }
    public Hoverable setOnHoverTickConsumer(Consumer<Float> consumer){
        onHoverTickConsumer = consumer;
        return this;
    }
    public Hoverable setOnUnhoveredConsumer(Runnable consumer){
        onUnhoveredConsumer = consumer;
        return this;
    }

    public Hoverable setOnHoverLine(String newLine){
        this.onHoverLine = newLine;
        return this;
    }
    public String getOnHoverLine(){
        return onHoverLine;
    }

    /** Hitbox */
    public Hitbox getHitbox(){
        return hb;
    }

    /** Position */
    @Override
    public Hoverable setPosition(Integer newPosX, Integer newPosY) {
        super.setPosition(newPosX, newPosY);
        hb.x = newPosX * Settings.xScale;
        hb.y = newPosY * Settings.yScale;

        return this;
    }

    /** Width and Height */
    @Override
    public Hoverable setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);

        hb.width = width * Settings.xScale;
        hb.height = height * Settings.yScale;

        return this;
    }

    /** Enabled */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        this.enabled = enabled;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
