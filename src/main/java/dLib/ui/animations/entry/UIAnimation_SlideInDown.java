package dLib.ui.animations.entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.UIElement;

public class UIAnimation_SlideInDown extends UIAnimation {

    private int origElementY = 0;

    public UIAnimation_SlideInDown(UIElement element) {
        super(element);
    }

    @Override
    public void start() {
        super.start();

        origElementY = element.getWorldPositionY();
        element.setWorldPositionY(1080 + element.getHeight());
    }

    @Override
    public void update() {
        float newPos = MathUtils.lerp(this.element.getWorldPositionY(), origElementY, Gdx.graphics.getDeltaTime() * 9.0F);

        element.setWorldPositionY((int) newPos);

        if (Math.abs(element.getWorldPositionY() - origElementY) < Settings.UI_SNAP_THRESHOLD) {
            isPlaying = false;
        }
    }

    @Override
    public void finishInstantly() {
        super.finishInstantly();

        element.setWorldPositionY(origElementY);
    }
}
