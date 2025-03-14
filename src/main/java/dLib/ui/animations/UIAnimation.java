package dLib.ui.animations;

import dLib.ui.elements.UIElement;

public abstract class UIAnimation {
    //region Variables

    protected UIElement element;

    protected EAnimationState state = EAnimationState.IDLE;

    //endregion

    //region Constructors

    public UIAnimation(UIElement element){
        this.element = element;
    }

    //endregion

    //region Methods

    public void start(){
        state = EAnimationState.PLAYING;
    }

    public abstract void update();

    public void finishInstantly(){
        state = EAnimationState.FINISHED;
    }

    public EAnimationState getAnimationState(){
        return state;
    }

    //endregion

    public enum EAnimationState{
        IDLE,
        PLAYING,
        FINISHED
    }
}
