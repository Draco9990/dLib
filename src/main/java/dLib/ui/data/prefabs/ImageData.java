package dLib.ui.data.prefabs;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.ImageScreenEditorItem;
import dLib.ui.data.implementations.HoverableData;
import dLib.ui.data.implementations.RenderableData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.elements.prefabs.Image;

import java.io.Serializable;

public class ImageData extends Hoverable.HoverableData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public UIElement makeUIElement() {
        return new Image(this);
    }
}
