package dLib.ui.animations.exit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.elements.UIElement;

public class UIAnimation_SlideOutLeft extends UIExitAnimation {

    private AnimationProperties properties;

    private int origElementX = 0;

    public UIAnimation_SlideOutLeft(UIElement element) {
        this(element, new AnimationProperties());
    }

    public UIAnimation_SlideOutLeft(UIElement element, AnimationProperties properties) {
        super(element);
        this.properties = properties;
    }

    @Override
    public void start() {
        super.start();

        origElementX = element.getWorldPositionX();
    }

    @Override
    public void update() {
        float lerpPos = MathUtils.lerp(element.getWorldPositionX(), origElementX + Math.max(element.getWidth() * 0.02f, 3), Gdx.graphics.getDeltaTime() * properties.speed);
        float lerpDistance = Math.abs(lerpPos - element.getWorldPositionX());
        float newPos = element.getWorldPositionX() - lerpDistance;

        element.setWorldPositionX(Math.round(newPos));

        if (element.getWorldPositionX() - Settings.UI_SNAP_THRESHOLD <= properties.refPointX - element.getWidth()) {
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
