package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.ui.themes.UITheme;

public class ListBoxUIPreview extends UIPreviewItem {
    public ListBoxUIPreview(){
        super(UITheme.whitePixel, 0, 0, 500, 500);
        setRenderColor(Color.LIGHT_GRAY);
    }

    public ListBoxUIPreview(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    @Override
    public UIPreviewItem makeCopy() {
        return (UIPreviewItem) new ListBoxUIPreview(image, x, y, width, height).setRenderColor(getColorForRender());
    }
}
