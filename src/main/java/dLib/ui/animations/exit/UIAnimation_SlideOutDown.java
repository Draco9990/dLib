package dLib.ui.animations.exit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.UIElement;

public class UIAnimation_SlideOutDown extends UIExitAnimation {

    private AnimationProperties properties;

    private int origElementY = 0;

    public UIAnimation_SlideOutDown(UIElement element) {
        this(element, new AnimationProperties());
    }

    public UIAnimation_SlideOutDown(UIElement element, AnimationProperties properties) {
        super(element);
        this.properties = properties;
    }

    @Override
    public void start() {
        super.start();

        origElementY = element.getWorldPositionY();
    }

    @Override
    public void update() {
        float lerpPos = Math.abs(MathUtils.lerp(element.getWorldPositionY(), origElementY + Math.max(element.getHeight() * 0.02f, 3), Gdx.graphics.getDeltaTime() * properties.speed));
        float absPos = Math.abs(element.getWorldPositionY());
        float lerpDistance = Math.abs(lerpPos - absPos);

        float newPos = element.getWorldPositionY() - lerpDistance;

        element.setWorldPositionY((int) newPos);

        if (element.getWorldPositionY() - Settings.UI_SNAP_THRESHOLD < properties.refPointY - element.getHeight()) {
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
