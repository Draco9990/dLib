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

    /** Dimensions */
    @Override
    public RenderableUIPreviewItem setDimensions(int newWidth, int newHeight) {
        super.setDimensions(newWidth, newHeight);

        getElementData().width = newWidth;
        getElementData().height = newHeight;

        return this;
    }

    /** Data */
    @Override
    public RenderableData makeElementData() {
        return new RenderableData();
    }

    @Override
    public RenderableData getElementData() {
        return (RenderableData) super.getElementData();
    }
}
