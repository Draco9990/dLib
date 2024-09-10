package dLib.ui.animations;

import dLib.ui.elements.UIElement;

public abstract class UIAnimation {
    //region Variables

    protected UIElement element;

    private boolean isPlaying = false;

    //endregion

    //region Constructors

    public UIAnimation(UIElement element){
        this.element = element;
    }

    //endregion

    //region Methods

    public void start(){
        isPlaying = false;
    }

    public abstract void update();

    public void finishInstantly(){
        isPlaying = true;
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    //endregion
}
