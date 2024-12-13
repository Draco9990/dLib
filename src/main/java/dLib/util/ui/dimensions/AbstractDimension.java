package dLib.util.ui.dimensions;

import dLib.ui.elements.UIElement;

public abstract class AbstractDimension {
    public AbstractDimension(){

    }

    public abstract int getWidth(UIElement self);
    public abstract int getHeight(UIElement self);

    public abstract void resizeWidthBy(UIElement self, int amount);
    public abstract void resizeHeightBy(UIElement self, int amount);

    public abstract AbstractDimension cpy();

    public abstract String getSimpleDisplayName();
}
