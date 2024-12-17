package dLib.util.ui.position;

import dLib.properties.ui.elements.IEditableValue;
import dLib.ui.elements.UIElement;
import dLib.util.bindings.Binding;

public abstract class AbstractPosition extends Binding implements IEditableValue {
    public AbstractPosition(){

    }

    public abstract int getLocalX(UIElement element);
    public abstract int getLocalY(UIElement element);

    public abstract AbstractPosition cpy();

    @Override
    public abstract String getDisplayValue();

    public abstract void offsetHorizontal(UIElement element, int amount);
    public abstract void offsetVertical(UIElement element, int amount);
}
