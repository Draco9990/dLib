package dLib.ui.animations.exit;

import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.UIElement;
import dLib.util.Reflection;

public abstract class UIExitAnimation extends UIAnimation {

    public UIExitAnimation(UIElement element) {
        super(element);
    }

    @Override
    public void onFinish() {
        super.onFinish();

        Reflection.invokeMethod("setVisibility", element, false);
    }
}
