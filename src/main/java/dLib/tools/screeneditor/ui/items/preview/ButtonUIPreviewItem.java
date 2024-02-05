package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.themes.UIThemeManager;

public class ButtonUIPreviewItem extends UIPreviewItem {
    public ButtonUIPreviewItem(){
        super(UIThemeManager.getDefaultTheme().button_small, 0, 0, 75, 75);
    }

    public ButtonUIPreviewItem(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    @Override
    public UIPreviewItem makeCopy() {
        return new ButtonUIPreviewItem(image, x, y, width, height);
    }
}
