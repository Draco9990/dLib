package dLib.ui.data.implementations;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;

import java.io.Serializable;

public class HoverableData extends RenderableData implements Serializable {
    private static final long serialVersionUID = 1L;


    @Override
    public Hoverable makeLiveInstance(Object... params) {
        return new Hoverable(this);
    }
}
