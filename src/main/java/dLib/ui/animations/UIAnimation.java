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

    public final void finishInstantly(){
        boolean needsCleanup = state == EAnimationState.PLAYING;

        state = EAnimationState.FINISHED;

        if(needsCleanup) {
            onFinish();
        }
    }
    protected void onFinish(){
    }

    public EAnimationState getAnimationState(){
        return state;
    }
    public void setAnimationState(EAnimationState state){
        this.state = state;
    }

    //endregion

    public enum EAnimationState{
        IDLE,
        PLAYING,
        FINISHED
    }
}
