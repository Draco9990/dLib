package dLib.ui.animations.entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.items.Renderable;

public class UIAnimation_FadeIn extends UIAnimation {

    private AnimationProperties properties;

    private float origColorAlpha;

    public UIAnimation_FadeIn(Renderable element) {
        this(element, new AnimationProperties());
    }

    public UIAnimation_FadeIn(Renderable element, AnimationProperties properties) {
        super(element);
        this.properties = properties;
    }

    public Renderable getElement(){
        return (Renderable)element;
    }

    @Override
    public void start() {
        super.start();

        origColorAlpha = getElement().getRenderColorAlphaMultiplier();
        getElement().setRenderColorAlphaMultiplier(0.0F);
    }

    @Override
    public void update() {
        float newAlpha = MathUtils.lerp(getElement().getRenderColorAlphaMultiplier(), origColorAlpha, Gdx.graphics.getDeltaTime() * properties.speed);

        getElement().setRenderColorAlphaMultiplier(newAlpha);

        if (newAlpha + 0.01 >= origColorAlpha){
            state = EAnimationState.FINISHED;
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();

        getElement().setRenderColorAlphaMultiplier(origColorAlpha);
    }

    public static class AnimationProperties{
        public float speed = 9.0F;
    }
}
