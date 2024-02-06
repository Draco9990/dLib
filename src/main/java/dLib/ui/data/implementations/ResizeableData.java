package dLib.ui.data.implementations;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Resizeable;

public class ResizeableData extends DraggableData{
    @Override
    public UIElement makeLiveInstance() {
        return new Resizeable(this);
    }
}
