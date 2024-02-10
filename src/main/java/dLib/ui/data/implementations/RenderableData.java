package dLib.ui.data.implementations;

import dLib.ui.data.UIElementData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.settings.prefabs.TextureSetting;

public class RenderableData extends UIElementData {
    public TextureSetting textureBinding;

    public String color;

    @Override
    public UIElement makeLiveInstance(Object... params) {
        return new Renderable(this);
    }
}
