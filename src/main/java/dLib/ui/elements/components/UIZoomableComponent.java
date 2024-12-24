package dLib.ui.elements.components;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;

public class UIZoomableComponent extends UIElementComponent<UIElement>{
    private boolean canZoom;

    private float scaleStep = 0.3f;
    private Float targetScaleX = null;
    private Float targetScaleY = null;


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
        }

        if(targetScaleX != null){
            float newScaleX = MathUtils.lerp(owner.getScaleX(), targetScaleX, 0.1f);
            if(Math.abs(newScaleX - targetScaleX) < 0.01f){
                newScaleX = targetScaleX;
                targetScaleX = null;
            }
            owner.setScaleX(newScaleX);

            float newScaleY = MathUtils.lerp(owner.getScaleY(), targetScaleY, 0.1f);
            if(Math.abs(newScaleY - targetScaleY) < 0.01f){
                newScaleY = targetScaleY;
                targetScaleY = null;
            }
            owner.setScaleY(newScaleY);
        }
    }
}
