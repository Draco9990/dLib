package dLib.ui.animations.entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.UIElement;

public class UIAnimation_SlideInDown extends UIAnimation {

    private AnimationProperties properties;

    private float origElementY = 0;

    public UIAnimation_SlideInDown(UIElement element) {
        this(element, new AnimationProperties());
    }

    public UIAnimation_SlideInDown(UIElement element, AnimationProperties properties) {
        super(element);
        this.properties = properties;
    }

    @Override
    public void start() {
        super.start();

        origElementY = element.getLocalPositionY();
        element.getLocalPositionYRaw().overrideCalculatedValue(properties.refPointY + element.getHeight());
    }

    @Override
    public void update() {
        float newPos = MathUtils.lerp(this.element.getLocalPositionY(), origElementY, Gdx.graphics.getDeltaTime() * properties.speed);

        element.getLocalPositionYRaw().overrideCalculatedValue(newPos);

        if (element.getLocalPositionY() - Settings.UI_SNAP_THRESHOLD <= origElementY) {
            state = EAnimationState.FINISHED;
        }
    }

    @Override
    public void finishInstantly() {
        super.finishInstantly();

        element.getLocalPositionYRaw().overrideCalculatedValue(origElementY);
    }

    public static class AnimationProperties{
        public int refPointY = 1080;

        public float speed = 9.0F;
    }
}
