package dLib.ui.data.implementations;

import dLib.ui.data.UIElementData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.bindings.texture.TextureBinding;

import java.io.Serializable;

public class RenderableData extends UIElementData implements Serializable {
    private static final long serialVersionUID = 1L;

    public TextureBinding textureBinding;

    public String renderColor;

    @Override
    public UIElement makeLiveInstance(Object... params) {
        return new Renderable(this);
    }
}
