package dLib.util.ui.dimensions;

import dLib.ui.elements.UIElement;

public abstract class AbstractDimension {
    public AbstractDimension(){

    }

    public abstract int getWidth(UIElement self);
    public abstract int getHeight(UIElement self);

    public abstract AbstractDimension cpy();
}
