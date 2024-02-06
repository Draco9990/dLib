package dLib.ui.data.implementations;

import dLib.ui.data.UIElementData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.TextureManager;

public class RenderableData extends UIElementData {
    public int width;
    public int height;

    public String texturePath;

    public String color;

    @Override
    public UIElement makeLiveInstance() {
        return new Renderable(this);
    }
}
