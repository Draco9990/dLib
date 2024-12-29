package dLib.ui.elements.components;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;
import dLib.util.IntegerVector2;

public class UIZoomableComponent extends UIElementComponent<UIElement>{
    private float minScale = 0.1f;
    private float maxScale = 2.0f;

    private float scaleStep = 0.1f;

    private Float targetScaleX = null;
    private Float targetScaleY = null;
    private int targetWorldMouseX;
    private int targetWorldMouseY;

    private boolean canZoom;

    @Override
    public void onRegisterComponent(UIElement owner) {
        super.onRegisterComponent(owner);

        owner.onHoveredEvent.subscribe(this, () -> {
            canZoom = true;
        });
        owner.onHoveredChildEvent.subscribe(this, (child) -> {
            UIElement parent = child.getParent();
            while(parent != null && parent != owner){
                if(parent.hasComponent(UIZoomableComponent.class)){
                    canZoom = false;
                    return;
                }
                parent = parent.getParent();
            }

            canZoom = true;
        });

        owner.onUnhoveredEvent.subscribe(this, () -> {
            canZoom = false;
        });
        owner.onUnhoveredChildEvent.subscribe(this, (child) -> {
            UIElement parent = child.getParent();
            while(parent != null && parent != owner){
                if(parent.hasComponent(UIZoomableComponent.class)){
                    return;
                }
                parent = parent.getParent();
            }

            if(owner.isHovered()){
                return;
            }
            canZoom = false;
        });
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        super.onUnregisterComponent(owner);

        owner.onHoveredEvent.unsubscribe(this);
        owner.onHoveredChildEvent.unsubscribe(this);
        owner.onUnhoveredEvent.unsubscribe(this);
        owner.onUnhoveredChildEvent.unsubscribe(this);
    }

    @Override
    public void onUpdate(UIElement owner) {
        super.onUpdate(owner);

        if(canZoom){
            if(InputHelper.scrolledUp){
                targetScaleX = owner.getScaleX() + scaleStep;
                targetScaleY = owner.getScaleY() + scaleStep;
            }
            else if(InputHelper.scrolledDown){
                targetScaleX = owner.getScaleX() - scaleStep;
                targetScaleY = owner.getScaleY() - scaleStep;
            }

            if(targetScaleX != null){
                targetScaleX = MathUtils.clamp(targetScaleX, minScale, maxScale);
            }
            if(targetScaleY != null){
                targetScaleY = MathUtils.clamp(targetScaleY, minScale, maxScale);
            }

            targetWorldMouseX = (int) (InputHelper.mX / Settings.xScale);
            targetWorldMouseY = (int) (InputHelper.mY / Settings.yScale);
        }

        if(targetScaleX != null && targetScaleY != null){
            IntegerVector2 localMouse = owner.worldToLocalUnscaled(new IntegerVector2(targetWorldMouseX, targetWorldMouseY));


            //Calculate and set the new scale x
            float newScaleX = MathUtils.lerp(owner.getScaleX(), targetScaleX, 0.2f);
            if(Math.abs(newScaleX - targetScaleX) < 0.01f){
                newScaleX = targetScaleX;
                targetScaleX = null;
            }
            if(newScaleX < 0.01f){
                newScaleX = 0.01f;
            }
            owner.setScaleX(newScaleX);

            //Calculate and set the new scale y
            float newScaleY = MathUtils.lerp(owner.getScaleY(), targetScaleY, 0.2f);
            if(Math.abs(newScaleY - targetScaleY) < 0.01f){
                newScaleY = targetScaleY;
                targetScaleY = null;
            }
            if(newScaleY < 0.01f){
                newScaleY = 0.01f;
            }
            owner.setScaleY(newScaleY);

            //Center the object around the mouse world position where we were zooming
            IntegerVector2 localMouseAfter = owner.worldToLocalUnscaled(new IntegerVector2(targetWorldMouseX, targetWorldMouseY));
            IntegerVector2 difference = new IntegerVector2(localMouseAfter.x - localMouse.x, localMouseAfter.y - localMouse.y);
            owner.offset((int) (difference.x * owner.getScaleX()), (int) (difference.y * owner.getScaleY()));
        }
    }

    //region Scale Settings

    public void setMinScale(float minScale){
        this.minScale = minScale;
    }

    public void setMaxScale(float maxScale){
        this.maxScale = maxScale;
    }

    public void setScaleStep(float scaleStep){
        this.scaleStep = scaleStep;
    }

    public float getMinScale(){
        return minScale;
    }

    public float getMaxScale(){
        return maxScale;
    }

    public float getScaleStep(){
        return scaleStep;
    }

    //endregion
}
