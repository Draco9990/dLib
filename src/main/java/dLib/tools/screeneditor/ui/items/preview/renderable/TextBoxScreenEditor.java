package dLib.tools.screeneditor.ui.items.preview.renderable;

import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.prefabs.TextBoxData;
import dLib.ui.themes.UITheme;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureThemeBinding;

public class TextBoxScreenEditor extends RenderableScreenEditorItem {
    /** Constructors */
    public TextBoxScreenEditor(){
        super(new TextureThemeBinding("button_large_outline_empty"), 0, 0, 300, 75);
    }

    public TextBoxScreenEditor(TextureBinding textureBinding, int xPos, int yPos, int width, int height) {
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
    public ScreenEditorItem makeCopy() {
        return new TextBoxScreenEditor(sTexture.getCurrentValue(), x, y, width, height);
    }
}
