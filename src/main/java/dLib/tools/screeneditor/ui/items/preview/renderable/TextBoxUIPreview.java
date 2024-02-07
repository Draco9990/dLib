package dLib.tools.screeneditor.ui.items.preview.renderable;

import com.badlogic.gdx.graphics.Texture;
import dLib.tools.screeneditor.ui.items.preview.RenderableUIPreviewItem;
import dLib.tools.screeneditor.ui.items.preview.UIPreviewItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.implementations.RenderableData;
import dLib.ui.data.prefabs.TextBoxData;
import dLib.ui.themes.UIThemeManager;

public class TextBoxUIPreview extends RenderableUIPreviewItem {
    /** Constructors */
    public TextBoxUIPreview(){
        super(UIThemeManager.getDefaultTheme().button_large_outline_empty, 0, 0, 300, 75);
    }

    public TextBoxUIPreview(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    /** Data */
    @Override
    public TextBoxData makeElementData() {
        return new TextBoxData();
    }

    @Override
    public TextBoxData getElementData() {
        return (TextBoxData) super.getElementData();
    }

    /** Copy */
    @Override
    public UIPreviewItem makeCopy() {
        return new TextBoxUIPreview(image, x, y, width, height);
    }
}
