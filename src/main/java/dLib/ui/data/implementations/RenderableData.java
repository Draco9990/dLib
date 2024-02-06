package dLib.ui.data.implementations;

import dLib.ui.data.UIElementData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;

public class RenderableData extends UIElementData {
    public int width;
    public int height;

    public String texturePath;

    public String color;

    @Override
    public UIElement makeLiveInstance(Object... params) {
        return new Renderable(this);
    }
}
