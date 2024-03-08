package dLib.ui.elements.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import dLib.modcompat.ModManager;
import dLib.ui.data.implementations.HoverableData;
import sayTheSpire.Output;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Hoverable extends Renderable{
    //region Variables

    protected Hitbox hb;

    private float totalHoverDuration;

    private String onHoverLine; // Say the Spire mod compatibility

    private ArrayList<Runnable> onHoveredConsumers = new ArrayList<>();
    private ArrayList<Consumer<Float>> onHoverTickConsumers = new ArrayList<>();
    private ArrayList<Runnable> onUnhoveredConsumers = new ArrayList<>();

    //endregion

    //region Constructors

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

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void update() {
        if(!shouldUpdate()) return;
        super.update();

        if(hb != null){
            boolean hbHoveredCache = this.hb.hovered || this.hb.justHovered;
            this.hb.move(getWorldPositionCenteredX() * Settings.xScale, getWorldPositionCenteredY() * Settings.yScale);
            this.hb.resize(getWidth() * Settings.xScale, getHeight() * Settings.yScale);
            this.hb.update();

            if(isEnabled()){
                if(this.hb.justHovered) onHovered();
                if(this.hb.hovered){
                    totalHoverDuration += Gdx.graphics.getDeltaTime();
                    onHoverTick(totalHoverDuration);
                }
            }

            if(hbHoveredCache && (!this.hb.hovered && !this.hb.justHovered)){
                onUnhovered();
            }
        }
    }

    @Override
    public void renderSelf(SpriteBatch sb) {
        super.renderSelf(sb);

        if(hb != null){
            hb.render(sb);
        }
    }

    //endregion

    //region Hover

    protected void onHovered(){
        totalHoverDuration = 0.f;

        if(getOnHoverLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnHoverLine(), true);
            }
        }
        for(Runnable consumer : onHoveredConsumers) consumer.run();
    }
    protected void onHoverTick(float totalTickDuration){
        for(Consumer<Float> consumer : onHoverTickConsumers) consumer.accept(totalTickDuration);
    }
    protected void onUnhovered(){
        totalHoverDuration = 0.f;

        for(Runnable consumer : onUnhoveredConsumers) consumer.run();
    }

    public boolean isHovered(){ return hb.hovered || hb.justHovered; }

    public Hoverable addOnHoveredConsumer(Runnable consumer){
        onHoveredConsumers.add(consumer);
        return this;
    }
    public Hoverable addOnHoverTickConsumer(Consumer<Float> consumer){
        onHoverTickConsumers.add(consumer);
        return this;
    }
    public Hoverable addOnUnhoveredConsumer(Runnable consumer){
        onUnhoveredConsumers.add(consumer);
        return this;
    }

    public Hoverable setOnHoverLine(String newLine){
        this.onHoverLine = newLine;
        return this;
    }
    public String getOnHoverLine(){
        return onHoverLine;
    }

    //endregion

    //region HitBox

    public Hitbox getHitbox(){
        return hb;
    }

    //endregion

    //endregion
}
