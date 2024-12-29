package dLib.util.ui.position;

import dLib.properties.ui.elements.IEditableValue;
import dLib.ui.elements.UIElement;
import dLib.util.bindings.Binding;

import java.io.Serializable;

public abstract class AbstractPosition extends Binding implements IEditableValue, Serializable {
    private static final long serialVersionUID = 1L;

    public AbstractPosition(){

    }

    public abstract int getLocalX(UIElement element);
    public abstract int getLocalY(UIElement element);

    public abstract void setValueFromString(String value);

    public abstract AbstractPosition cpy();

    @Override
    public abstract String getDisplayValue();

    public abstract void offsetHorizontal(UIElement element, int amount);
    public abstract void offsetVertical(UIElement element, int amount);
}
