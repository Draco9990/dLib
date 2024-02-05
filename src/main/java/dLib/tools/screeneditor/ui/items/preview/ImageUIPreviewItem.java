package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.themes.UITheme;

public class ImageUIPreviewItem extends UIPreviewItem {
    public ImageUIPreviewItem(){
        super(UITheme.whitePixel, 0, 0, 75, 75);
    }

    public ImageUIPreviewItem(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    @Override
    public UIPreviewItem makeCopy() {
        return new ImageUIPreviewItem(image, x, y, width, height);
    }
}
