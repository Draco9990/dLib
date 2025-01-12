package dLib.util.ui.dimensions;

import dLib.properties.ui.elements.IEditableValue;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public abstract class AbstractDimension implements IEditableValue, Serializable {
    private static final long serialVersionUID = 1L;

    protected ReferenceDimension refDimension;

    public AbstractDimension(){

    }

    public abstract int calculateDimension(UIElement self);
    public abstract void resizeBy(UIElement self, int amount);

    public abstract void setValueFromString(String value);

    public abstract AbstractDimension cpy();

    public abstract String getSimpleDisplayName();

    public void setReferenceDimension(ReferenceDimension dimension){
        this.refDimension = dimension;
    }

    public enum ReferenceDimension {
        WIDTH,
        HEIGHT
    }
}
