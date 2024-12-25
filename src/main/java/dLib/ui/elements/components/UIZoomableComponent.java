package dLib.ui.elements.components;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;
import dLib.util.IntegerVector2;

public class UIZoomableComponent extends UIElementComponent<UIElement>{
    private boolean canZoom;

    private float scaleStep = 0.1f;

    private Float targetScaleX = null;
    private Float targetScaleY = null;
    private int targetWorldMouseX;
    private int targetWorldMouseY;

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

            targetWorldMouseX = (int) (InputHelper.mX / Settings.xScale);
            targetWorldMouseY = (int) (InputHelper.mY / Settings.yScale);
        }

        if(targetScaleX != null && targetScaleY != null){
            IntegerVector2 localMouse = owner.worldToLocal2(new IntegerVector2(targetWorldMouseX, targetWorldMouseY));

            //Calculate and set the new scale x
            float newScaleX = targetScaleX;//float newScaleX = MathUtils.lerp(owner.getScaleX(), targetScaleX, 0.2f);
            if(Math.abs(newScaleX - targetScaleX) < 0.01f){
                newScaleX = targetScaleX;
                targetScaleX = null;
            }
            if(newScaleX < 0.01f){
                newScaleX = 0.01f;
            }
            owner.setScaleX(newScaleX);

            //Calculate and set the new scale y
            float newScaleY = targetScaleY;//float newScaleY = MathUtils.lerp(owner.getScaleY(), targetScaleY, 0.2f);
            if(Math.abs(newScaleY - targetScaleY) < 0.01f){
                newScaleY = targetScaleY;
                targetScaleY = null;
            }
            if(newScaleY < 0.01f){
                newScaleY = 0.01f;
            }
            owner.setScaleY(newScaleY);

            //Center the object around the mouse world position where we were zooming
            IntegerVector2 worldMouseAfterRecenter = owner.localToWorld2(localMouse);
            IntegerVector2 difference = new IntegerVector2(worldMouseAfterRecenter.x - targetWorldMouseX, worldMouseAfterRecenter.y - targetWorldMouseY);
            owner.offset(difference.x, difference.y);
        }
    }
}
