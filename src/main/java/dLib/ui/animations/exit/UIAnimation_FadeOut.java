package dLib.ui.animations.exit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import dLib.ui.elements.items.Renderable;

public class UIAnimation_FadeOut extends UIExitAnimation {

    private AnimationProperties properties;

    private float origColorAlpha;

    public UIAnimation_FadeOut(Renderable element) {
        this(element, new AnimationProperties());
    }

    public UIAnimation_FadeOut(Renderable element, AnimationProperties properties) {
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
    }

    @Override
    public void update() {
        float newAlpha = MathUtils.lerp(getElement().getRenderColorAlphaMultiplier(), 0, Gdx.graphics.getDeltaTime() * properties.speed);

        getElement().setRenderColorAlphaMultiplier(newAlpha);

        if (newAlpha - 0.01 <= 0){
            isPlaying = false;
        }
    }

    @Override
    public void finishInstantly() {
        super.finishInstantly();

        getElement().setRenderColorAlphaMultiplier(origColorAlpha);
        getElement().hideInstantly();
    }

    public static class AnimationProperties{
        public float speed = 9.0F;
    }
}
