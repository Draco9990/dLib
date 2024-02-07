package dLib.tools.screeneditor.ui.items.preview.renderable;

import com.badlogic.gdx.graphics.Texture;
import dLib.tools.screeneditor.ui.items.preview.UIPreviewItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.ButtonData;
import dLib.ui.themes.UIThemeManager;

public class ButtonUIPreviewItem extends UIPreviewItem {
    /** Constructors */
    public ButtonUIPreviewItem(){
        super(UIThemeManager.getDefaultTheme().button_small, 0, 0, 75, 75);
    }

    public ButtonUIPreviewItem(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    /** Data */
    @Override
    public ButtonData makeElementData() {
        return new ButtonData();
    }

    @Override
    public ButtonData getElementData() {
        return (ButtonData) super.getElementData();
    }

    /** Copy */
    @Override
    public UIPreviewItem makeCopy() {
        return new ButtonUIPreviewItem(image, x, y, width, height);
    }

}
