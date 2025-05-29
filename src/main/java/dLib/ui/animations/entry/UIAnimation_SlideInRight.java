package dLib.ui.animations.entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.UIElement;

public class UIAnimation_SlideInRight extends UIAnimation {

    private AnimationProperties properties;

    private float origElementX = 0;

    public UIAnimation_SlideInRight(UIElement element) {
        this(element, new AnimationProperties());
    }

    public UIAnimation_SlideInRight(UIElement element, AnimationProperties properties) {
        super(element);
        this.properties = properties;
    }

    @Override
    public void start() {
        super.start();

        origElementX = element.getLocalPositionX();
        element.getLocalPositionXRaw().overrideCalculatedValue(properties.refPointX - element.getWidth());
    }

    @Override
    public void update() {
        float newPos = MathUtils.lerp(this.element.getLocalPositionX(), origElementX, Gdx.graphics.getDeltaTime() * properties.speed);

        element.getLocalPositionXRaw().overrideCalculatedValue(newPos);

        if (element.getWorldPositionX() + Settings.UI_SNAP_THRESHOLD >= origElementX) {
            state = EAnimationState.FINISHED;
        }
    }

    @Override
    public void finishInstantly() {
        super.finishInstantly();

        element.getLocalPositionXRaw().overrideCalculatedValue(origElementX);
    }

    public static class AnimationProperties{
        public int refPointX = 0;

        public float speed = 9.0F;
    }
}
