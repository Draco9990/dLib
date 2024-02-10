package dLib.tools.screeneditor.ui.items.preview.renderable;

import dLib.tools.screeneditor.ui.items.preview.RenderableUIPreviewItem;
import dLib.tools.screeneditor.ui.items.preview.UIPreviewItem;
import dLib.ui.data.prefabs.TextBoxData;
import dLib.ui.themes.UITheme;
import dLib.util.bindings.image.TextureBinding;
import dLib.util.bindings.image.TextureThemeBinding;

public class TextBoxUIPreview extends RenderableUIPreviewItem {
    /** Constructors */
    public TextBoxUIPreview(){
        super(new TextureThemeBinding("button_large_outline_empty", UITheme.class), 0, 0, 300, 75);
    }

    public TextBoxUIPreview(TextureBinding textureBinding, int xPos, int yPos, int width, int height) {
        super(textureBinding, xPos, yPos, width, height);
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
        return new TextBoxUIPreview(sTexture.getCurrentValue(), x, y, width, height);
    }
}
