package dLib.ui.data.prefabs;

import dLib.ui.data.implementations.RenderableData;
import dLib.ui.elements.prefabs.Image;

import java.io.Serializable;

public class ImageData extends RenderableData  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public Image makeLiveInstance(Object... params) {
        return new Image(this);
    }
}
