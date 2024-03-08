package dLib.ui.data.implementations;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Draggable;

import java.io.Serializable;

public class DraggableData extends InteractableData implements Serializable {
    private static final long serialVersionUID = 1L;

    public boolean canDragX;
    public boolean canDragY;

    public Integer lowerBoundX; //TODO RF
    public Integer upperBoundX;

    public Integer lowerBoundY;
    public Integer upperBoundY;

    @Override
    public Draggable makeLiveInstance(Object... params) {
        return new Draggable(this);
    }
}
