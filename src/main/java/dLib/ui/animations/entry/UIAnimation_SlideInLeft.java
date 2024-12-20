package dLib.ui.animations.entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.UIElement;

public class UIAnimation_SlideInLeft extends UIAnimation {

    private AnimationProperties properties;

    private int origElementX = 0;

    public UIAnimation_SlideInLeft(UIElement element) {
        this(element, new AnimationProperties());
    }

    public UIAnimation_SlideInLeft(UIElement element, AnimationProperties properties) {
        super(element);
        this.properties = properties;
    }

    @Override
    public void start() {
        super.start();

        origElementX = element.getLocalPositionX();
        element.setLocalPositionX(properties.refPointX + element.getWidth());
    }

    @Override
    public void update() {
        float newPos = MathUtils.lerp(this.element.getLocalPositionX(), origElementX, Gdx.graphics.getDeltaTime() * properties.speed);

        element.setLocalPositionX((int) newPos);

        if (element.getLocalPositionX() - Settings.UI_SNAP_THRESHOLD <= origElementX) {
            isPlaying = false;
        }
    }

    @Override
    public void finishInstantly() {
        super.finishInstantly();

        element.setLocalPositionX(origElementX);
    }

    public static class AnimationProperties{
        public int refPointX = 1920;

        public float speed = 9.0F;
    }
}
