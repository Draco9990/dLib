package dLib.ui.data.implementations;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;

public class HoverableData extends RenderableData{

    @Override
    public Hoverable makeLiveInstance(Object... params) {
        return new Hoverable(this);
    }
}
