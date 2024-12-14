package dLib.util.ui.position;

import dLib.ui.elements.UIElement;
import dLib.util.bindings.Binding;
import dLib.util.ui.dimensions.FillDimension;

public abstract class AbstractPosition extends Binding {
    public AbstractPosition(){

    }

    public abstract int getLocalX(UIElement element);
    public abstract int getLocalY(UIElement element);

    public abstract AbstractPosition cpy();

    @Override
    public String getShortDisplayName() {
        return getFullDisplayName();
    }
    @Override
    public abstract String getFullDisplayName();

    public abstract void offsetHorizontal(UIElement element, int amount);
    public abstract void offsetVertical(UIElement element, int amount);
}
