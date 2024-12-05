package dLib.util.ui.padding;

import dLib.ui.elements.UIElement;

public abstract class AbstractPadding {
    public AbstractPadding(){

    }

    public abstract int getHorizontal(UIElement owner);
    public abstract int getVertical(UIElement owner);

    public abstract AbstractPadding cpy();
}
