package dLib.tools.screeneditor.ui.items.preview.renderable;

import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.prefabs.ButtonData;
import dLib.ui.themes.UITheme;
import dLib.util.Reflection;
import dLib.util.bindings.image.TextureBinding;
import dLib.util.bindings.image.TextureThemeBinding;

public class ButtonScreenEditorItem extends RenderableScreenEditorItem {
    /** Constructors */
    public ButtonScreenEditorItem(){
        super(new TextureThemeBinding(Reflection.getFieldByName("button_small", UITheme.class)), 0, 0, 75, 75);
    }

    public ButtonScreenEditorItem(TextureBinding textureBinding, int xPos, int yPos, int width, int height) {
        super(textureBinding, xPos, yPos, width, height);
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
    public ScreenEditorItem makeCopy() {
        return new ButtonScreenEditorItem(sTexture.getCurrentValue(), x, y, width, height);
    }

}
