package dLib.ui.elements.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import dLib.modcompat.ModManager;
import dLib.patches.InputHelperHoverConsumer;
import dLib.ui.screens.ScreenManager;
import dLib.util.IntegerVector4;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;
import sayTheSpire.Output;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Hoverable extends Renderable{
    //region Variables

    protected Hitbox hb;

    private boolean isClickthrough = false;

    private ArrayList<Runnable> onHoveredConsumers = new ArrayList<>();
    private ArrayList<Consumer<Float>> onHoverTickConsumers = new ArrayList<>();
    private ArrayList<Runnable> onUnhoveredConsumers = new ArrayList<>();

    private String onHoverLine; // Say the Spire mod compatibility

    private float totalHoverDuration;

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

        onHoveredConsumers.add(() -> data.onHovered.executeBinding(ScreenManager.getCurrentScreen()));
        onHoverTickConsumers.add((elapsedTime) -> data.onHoverTick.executeBinding(ScreenManager.getCurrentScreen(), elapsedTime));
        onUnhoveredConsumers.add(() -> data.onUnhovered.executeBinding(ScreenManager.getCurrentScreen()));

        this.isClickthrough = data.isClickthrough;
    }

    private void initialize(){
        hb = new Hitbox(0, 0, getWidth() * Settings.xScale, getHeight() * Settings.yScale);
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void updateSelf() {
        super.updateSelf();

        if(hb != null){
            boolean hbHoveredCache = this.hb.hovered || this.hb.justHovered;

            float targetHbX = getWorldPositionCenteredX() * Settings.xScale;
            float targetHbY = getWorldPositionCenteredY() * Settings.yScale;
            float targetHbWidth = getWidth() * Settings.xScale;
            float targetHbHeight = getHeight() * Settings.yScale;

            IntegerVector4 maskBounds = getMaskWorldBounds();
            if(maskBounds != null && overlaps(maskBounds) && !within(maskBounds)){
                if(getWorldPositionX() < maskBounds.x){
                    float newTargetHbX = (maskBounds.x + getWidth() * 0.5f) * Settings.xScale;
                    targetHbWidth -= newTargetHbX - targetHbX;
                    targetHbX = newTargetHbX;
                }
                if(getWorldPositionY() < maskBounds.y){
                    float newTargetHbY = (maskBounds.y + getHeight() * 0.5f) * Settings.yScale;
                    targetHbHeight -= newTargetHbY - targetHbY;
                    targetHbY = newTargetHbY;
                }


                if(getWorldPositionX() + getWidth() > maskBounds.x + maskBounds.w){
                    targetHbWidth = (maskBounds.x + maskBounds.w - getWorldPositionX()) * Settings.xScale;
                }
                if(getWorldPositionY() + getHeight() > maskBounds.y + maskBounds.h){
                    targetHbHeight = (maskBounds.y + maskBounds.h - getWorldPositionY()) * Settings.yScale;
                    targetHbY = (maskBounds.y + maskBounds.h - (targetHbHeight / Settings.yScale * 0.5f)) * Settings.yScale;
                }
            }

            this.hb.resize(targetHbWidth, targetHbHeight);
            this.hb.move(targetHbX, targetHbY);
            this.hb.update();

            if((this.hb.justHovered || this.hb.hovered) && InputHelperHoverConsumer.alreadyHovered){
                this.hb.justHovered = false;
                this.hb.hovered = false;
            }

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

        if(!isClickthrough()) InputHelperHoverConsumer.alreadyHovered = true;
    }
    protected void onHoverTick(float totalTickDuration){
        for(Consumer<Float> consumer : onHoverTickConsumers) consumer.accept(totalTickDuration);

        if(!isClickthrough()) InputHelperHoverConsumer.alreadyHovered = true;
    }
    protected void onUnhovered(){
        totalHoverDuration = 0.f;

        for(Runnable consumer : onUnhoveredConsumers) consumer.run();
    }

    public boolean isHovered(){ return (hb.hovered || hb.justHovered); }

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

    //region Clickthrough

    public Hoverable setClickthrough(boolean newValue){
        isClickthrough = newValue;
        return this;
    }

    public boolean isClickthrough(){
        return isClickthrough || (!isVisible() && !isEnabled());
    }

    //endregion

    //endregion

    public static class HoverableData extends Renderable.RenderableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public MethodBinding onHovered = new NoneMethodBinding();
        public MethodBinding onHoverTick = new NoneMethodBinding();
        public MethodBinding onUnhovered = new NoneMethodBinding();

        public boolean isClickthrough = false;

        @Override
        public Hoverable makeUIElement() {
            return new Hoverable(this);
        }
    }
}
