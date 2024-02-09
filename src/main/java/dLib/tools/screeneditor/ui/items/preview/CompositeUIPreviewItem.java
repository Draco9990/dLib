package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.data.CompositeUIElementData;

public abstract class CompositeUIPreviewItem extends UIPreviewItem{
    /** Constructors */
    public CompositeUIPreviewItem(Texture image) {
        super(image);
    }

    public CompositeUIPreviewItem(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
    }

    public CompositeUIPreviewItem(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    /** Position & Dimensions */
    @Override
    public CompositeUIPreviewItem setPosition(Integer newPosX, Integer newPosY) {
        super.setPosition(newPosX, newPosY);
        // if composites are ever available for user adding, implement this with properties
        return this;
    }

    @Override
    public CompositeUIPreviewItem setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);
        // if composites are ever available for user adding, implement this with properties
        return this;
    }

    /** Data */
    @Override
    public CompositeUIElementData makeElementData() {
        CompositeUIElementData data = new CompositeUIElementData();
        data.ID = getId();
        data.x = x;
        data.y = y;
        data.width = width;
        data.height = height;
        return data;
    }

    @Override
    public CompositeUIElementData getElementData() {
        return (CompositeUIElementData) super.getElementData();
    }
}
