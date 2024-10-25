package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Draggable;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class Scrollbar extends UIElement {
    //region Variables

    protected Draggable slider;

    private ArrayList<Consumer<Float>> onScrollbarScrolledListeners = new ArrayList<>();

    //endregion

    //region Constructors

    public Scrollbar(int x, int y, int width, int height){
        super(x, y, width, height);

        slider = buildSlider(width, height);
    }

    protected abstract Draggable buildSlider(int containerWidth, int containerHeight);

    //endregion

    //region Methods

    public void onScrollbarScrolled(float percentage){
        for(Consumer<Float> listener : onScrollbarScrolledListeners){
            listener.accept(percentage);
        }
    }

    public void addOnScrollbarScrolledListener(Consumer<Float> listener){
        onScrollbarScrolledListeners.add(listener);
    }

    public abstract void reset();

    //region Slider

    public Draggable getSlider(){
        return slider;
    }

    //endregion

    //endregion
}
