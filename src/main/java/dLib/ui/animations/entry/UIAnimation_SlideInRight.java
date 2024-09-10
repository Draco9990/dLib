package dLib.ui.animations.entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.UIElement;

public class UIAnimation_SlideInRight extends UIAnimation {

    private int origElementX = 0;

    public UIAnimation_SlideInRight(UIElement element) {
        super(element);
    }

    @Override
    public void start() {
        super.start();

        origElementX = element.getWorldPositionX();
        element.setWorldPositionX(-element.getWidth());
    }

    @Override
    public void update() {
        float newPos = MathUtils.lerp(this.element.getWorldPositionX(), origElementX, Gdx.graphics.getDeltaTime() * 9.0F);

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
}
