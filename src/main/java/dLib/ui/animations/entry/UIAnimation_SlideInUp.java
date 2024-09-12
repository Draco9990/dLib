package dLib.ui.animations.entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.UIElement;

public class UIAnimation_SlideInUp extends UIAnimation {

    private AnimationProperties properties;

    private int origElementY = 0;

    public UIAnimation_SlideInUp(UIElement element) {
        this(element, new AnimationProperties());
    }

    public UIAnimation_SlideInUp(UIElement element, AnimationProperties properties) {
        super(element);
        this.properties = properties;
    }

    @Override
    public void start() {
        super.start();

        origElementY = element.getWorldPositionY();
        element.setWorldPositionY(properties.refPointY - element.getHeight());
    }

    @Override
    public void update() {
        float newPos = MathUtils.lerp(this.element.getWorldPositionY(), origElementY, Gdx.graphics.getDeltaTime() * properties.speed);

        element.setWorldPositionY((int) newPos);

        if (Math.abs(element.getLocalPositionY() - origElementY) < Settings.UI_SNAP_THRESHOLD) {
            isPlaying = false;
        }
    }

    @Override
    public void finishInstantly() {
        super.finishInstantly();

        element.setWorldPositionY(origElementY);
    }

    public static class AnimationProperties{
        public int refPointY = 0;

        public float speed = 9.0F;
    }
}
