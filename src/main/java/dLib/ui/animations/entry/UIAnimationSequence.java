package dLib.ui.animations.entry;

import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.UIElement;

import java.util.ArrayList;
import java.util.Arrays;

public class UIAnimationSequence extends UIAnimation {
    private ArrayList<UIAnimation> animations = new ArrayList<>();
    private UIAnimation currentAnimation;

    public UIAnimationSequence(UIElement forElement, UIAnimation... animations) {
        super(forElement);

        this.animations.addAll(Arrays.asList(animations));
    }

    @Override
    public void update() {
        if(currentAnimation == null && !animations.isEmpty()){
            currentAnimation = animations.remove(0);
            currentAnimation.start();
        }

        if(currentAnimation != null){
            currentAnimation.update();

            if(currentAnimation.getAnimationState() == EAnimationState.FINISHED){
                currentAnimation.finishInstantly();
                currentAnimation = null;

                if(animations.isEmpty()){
                    state = EAnimationState.FINISHED;
                }
            }
        }
    }
}
