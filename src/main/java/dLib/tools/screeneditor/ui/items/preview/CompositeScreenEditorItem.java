package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.data.CompositeUIElementData;

public abstract class CompositeScreenEditorItem extends ScreenEditorItem {
    /** Constructors */
    public CompositeScreenEditorItem(Texture image) {
        super(image);
    }

    public CompositeScreenEditorItem(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
    }

    public CompositeScreenEditorItem(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    public CompositeScreenEditorItem(CompositeUIElementData data){
        super(data);
    }

    /** Position & Dimensions */
    @Override
    public CompositeScreenEditorItem setPosition(Integer newPosX, Integer newPosY) {
        super.setPosition(newPosX, newPosY);
        // if composites are ever available for user adding, implement this with properties
        return this;
    }

    @Override
    public CompositeScreenEditorItem setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);
        // if composites are ever available for user adding, implement this with properties
        return this;
    }

    /** Data */
    @Override
    public CompositeUIElementData makeElementData() {
        return new CompositeUIElementData();
    }

    @Override
    public CompositeUIElementData getElementData() {
        return (CompositeUIElementData) super.getElementData();
    }
}
