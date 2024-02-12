package dLib.ui.data.implementations;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Resizeable;

import java.io.Serializable;

public class ResizeableData extends DraggableData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public Resizeable makeLiveInstance(Object... params) {
        return new Resizeable(this);
    }
}
