package dLib.ui.animations.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.UIElement;
import dLib.util.Timer;

public class UIAnimation_Blinking extends UIAnimation {
    private AnimationProperties properties;

    private Timer timer;

    public UIAnimation_Blinking(UIElement element) {
        this(element, new AnimationProperties());
    }

    public UIAnimation_Blinking(UIElement element, AnimationProperties properties) {
        super(element);
        this.properties = properties;
    }

    @Override
    public void start() {
        super.start();

        timer = new Timer(properties.period);
        timer.onTriggerEvent.subscribe(this, () -> element.toggleVisibility());
    }

    @Override
    public void update() {
        timer.update();
    }

    @Override
    public void finishInstantly() {
        super.finishInstantly();

        element.showInstantly();
    }

    public static class AnimationProperties{
        public float period = 0.5F;
    }
}
