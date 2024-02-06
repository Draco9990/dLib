package dLib.ui.data.prefabs;

import dLib.ui.data.implementations.RenderableData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.Image;

public class ImageData extends RenderableData {
    @Override
    public UIElement makeLiveInstance() {
        return new Image(this);
    }
}
