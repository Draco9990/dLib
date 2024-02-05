package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.themes.UIThemeManager;

public class TextBoxUIPreview extends UIPreviewItem {
    public TextBoxUIPreview(){
        super(UIThemeManager.getDefaultTheme().button_large_outline_empty, 0, 0, 300, 75);
    }

    public TextBoxUIPreview(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    @Override
    public UIPreviewItem makeCopy() {
        return new TextBoxUIPreview(image, x, y, width, height);
    }
}
