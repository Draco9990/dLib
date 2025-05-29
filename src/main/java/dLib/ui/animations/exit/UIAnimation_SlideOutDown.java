package dLib.ui.animations.exit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.elements.UIElement;

public class UIAnimation_SlideOutDown extends UIExitAnimation {

    private AnimationProperties properties;

    private float origElementY = 0;

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

        origElementY = element.getLocalPositionY();
    }

    @Override
    public void update() {
        float lerpPos = Math.abs(MathUtils.lerp(element.getLocalPositionY(), origElementY + Math.max(element.getHeight() * 0.02f, 3), Gdx.graphics.getDeltaTime() * properties.speed));
        float absPos = Math.abs(element.getLocalPositionY());
        float lerpDistance = Math.abs(lerpPos - absPos);

        float newPos = element.getLocalPositionY() - lerpDistance;

        element.getLocalPositionYRaw().overrideCalculatedValue(newPos);

        if (element.getLocalPositionY() - Settings.UI_SNAP_THRESHOLD < properties.refPointY - element.getHeight()) {
            state = EAnimationState.FINISHED;
        }
    }

    @Override
    public void finishInstantly() {
        super.finishInstantly();

        element.getLocalPositionYRaw().overrideCalculatedValue(origElementY);
    }

    public static class AnimationProperties{
        public int refPointY = 0;

        public float speed = 9.0F;
    }
}
