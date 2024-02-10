package dLib.tools.screeneditor.ui.items.preview.renderable;

import dLib.tools.screeneditor.ui.items.preview.RenderableUIPreviewItem;
import dLib.tools.screeneditor.ui.items.preview.UIPreviewItem;
import dLib.ui.data.prefabs.ImageData;
import dLib.ui.themes.UITheme;
import dLib.util.bindings.image.TextureBinding;
import dLib.util.bindings.image.TextureThemeBinding;

public class ImageUIPreviewItem extends RenderableUIPreviewItem {
    /** Constructors */
    public ImageUIPreviewItem(){
        super(new TextureThemeBinding("whitePixel", UITheme.class), 0, 0, 75, 75);
    }

    public ImageUIPreviewItem(TextureBinding textureBinding, int xPos, int yPos, int width, int height) {
        super(textureBinding, xPos, yPos, width, height);
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
        return new ImageUIPreviewItem(sTexture.getCurrentValue(), x, y, width, height);
    }
}
