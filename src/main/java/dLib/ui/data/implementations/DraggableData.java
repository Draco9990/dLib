package dLib.ui.data.implementations;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Draggable;

public class DraggableData extends InteractableData{
    public boolean canDragX;
    public boolean canDragY;

    public Integer lowerBoundX;
    public Integer upperBoundX;

    public Integer lowerBoundY;
    public Integer upperBoundY;

    @Override
    public UIElement makeLiveInstance() {
        return new Draggable(this);
    }
}
