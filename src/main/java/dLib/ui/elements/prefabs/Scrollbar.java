package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Draggable;

public abstract class Scrollbar extends UIElement {
    //region Variables

    protected Draggable slider;

    //endregion

    //region Constructors

    public Scrollbar(int x, int y, int width, int height){
        super(x, y, width, height);

        slider = buildSlider(width, height);
    }

    protected abstract Draggable buildSlider(int containerWidth, int containerHeight);

    //endregion

    //region Methods

    public abstract void onScrollbarScrolled(float percentage); //TODO add listeners

    public abstract void reset();

    //region Slider

    public Draggable getSlider(){
        return slider;
    }

    //endregion

    //endregion
}
