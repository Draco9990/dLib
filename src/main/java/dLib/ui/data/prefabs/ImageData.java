package dLib.ui.data.prefabs;

import dLib.ui.data.implementations.RenderableData;
import dLib.ui.elements.prefabs.Image;

public class ImageData extends RenderableData {
    @Override
    public Image makeLiveInstance(Object... params) {
        return new Image(this);
    }
}
