package dLib.ui.animations.exit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.elements.UIElement;

public class UIAnimation_SlideOutRight extends UIExitAnimation {

    private AnimationProperties properties;

    private float origElementX = 0;

    public UIAnimation_SlideOutRight(UIElement element) {
        this(element, new AnimationProperties());
    }

    public UIAnimation_SlideOutRight(UIElement element, AnimationProperties properties) {
        super(element);
        this.properties = properties;
    }

    @Override
    public void start() {
        super.start();

        origElementX = element.getLocalPositionX();
    }

    @Override
    public void update() {
        float lerpPos = MathUtils.lerp(element.getLocalPositionX(), origElementX - Math.max(element.getWidth() * 0.02f, 3), Gdx.graphics.getDeltaTime() * properties.speed);
        float lerpDistance = Math.abs(lerpPos - element.getLocalPositionX());
        float newPos = element.getLocalPositionX() + lerpDistance;

        element.getLocalPositionXRaw().overrideCalculatedValue(Math.round(newPos));

        if (element.getLocalPositionX() + Settings.UI_SNAP_THRESHOLD >= properties.refPointX) {
            state = EAnimationState.FINISHED;
        }
    }

    @Override
    public void finishInstantly() {
        super.finishInstantly();

        element.getLocalPositionXRaw().overrideCalculatedValue(origElementX);
    }

    public static class AnimationProperties{
        public int refPointX = 1920;

        public float speed = 9.0F;
    }
}
