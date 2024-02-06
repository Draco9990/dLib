package dLib.ui.data.implementations;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;

public class HoverableData extends RenderableData{

    @Override
    public UIElement makeLiveInstance() {
        return new Hoverable(this);
    }
}
