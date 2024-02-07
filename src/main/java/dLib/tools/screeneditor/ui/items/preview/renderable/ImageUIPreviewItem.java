package dLib.tools.screeneditor.ui.items.preview.renderable;

import com.badlogic.gdx.graphics.Texture;
import dLib.tools.screeneditor.ui.items.preview.RenderableUIPreviewItem;
import dLib.tools.screeneditor.ui.items.preview.UIPreviewItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.ImageData;
import dLib.ui.themes.UITheme;

public class ImageUIPreviewItem extends RenderableUIPreviewItem {
    /** Constructors */
    public ImageUIPreviewItem(){
        super(UITheme.whitePixel, 0, 0, 75, 75);
    }

    public ImageUIPreviewItem(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    /** Data */
    @Override
    public ImageData makeElementData() {
        return new ImageData();
    }

    @Override
    public ImageData getElementData() {
        return (ImageData) super.getElementData();
    }

    /** Copy */
    @Override
    public UIPreviewItem makeCopy() {
        return new ImageUIPreviewItem(image, x, y, width, height);
    }
}
