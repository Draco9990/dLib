package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.data.UIElementData;
import dLib.ui.data.implementations.RenderableData;
import dLib.ui.elements.implementations.Resizeable;

public abstract class RenderableUIPreviewItem extends UIPreviewItem{
    /** Constructors */
    public RenderableUIPreviewItem(Texture image) {
        super(image);
    }

    public RenderableUIPreviewItem(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
    }

    public RenderableUIPreviewItem(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    /** Data */
    @Override
    public RenderableData makeElementData() {
        RenderableData data = new RenderableData();
        data.x = x;
        data.y = y;
        return data;
    }

    @Override
    public RenderableData getElementData() {
        return (RenderableData) super.getElementData();
    }
}
