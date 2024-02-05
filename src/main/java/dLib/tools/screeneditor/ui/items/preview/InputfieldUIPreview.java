package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.themes.UIThemeManager;

public class InputfieldUIPreview extends UIPreviewItem {
    public InputfieldUIPreview(){
        super(UIThemeManager.getDefaultTheme().inputfield, 0, 0, 500, 75);
    }

    public InputfieldUIPreview(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    @Override
    public UIPreviewItem makeCopy() {
        return new InputfieldUIPreview(image, x, y, width, height);
    }
}
