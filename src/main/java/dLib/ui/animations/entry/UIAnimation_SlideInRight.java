package dLib.ui.animations.entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.UIElement;

public class UIAnimation_SlideInRight extends UIAnimation {

    private AnimationProperties properties;

    private int origElementX = 0;

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

        origElementX = element.getWorldPositionX();
        element.setWorldPositionX(properties.refPointX - element.getWidth());
    }

    @Override
    public void update() {
        float newPos = MathUtils.lerp(this.element.getWorldPositionX(), origElementX, Gdx.graphics.getDeltaTime() * properties.speed);

        element.setWorldPositionX((int) newPos);

        if (Math.abs(element.getWorldPositionX() - origElementX) < Settings.UI_SNAP_THRESHOLD) {
            isPlaying = false;
        }
    }

    @Override
    public void finishInstantly() {
        super.finishInstantly();

        element.setWorldPositionX(origElementX);
    }

    public static class AnimationProperties{
        public int refPointX = 0;

        public float speed = 9.0F;
    }
}
