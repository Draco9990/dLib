package dLib.tools.screeneditor.ui.items.preview.renderable;

import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.ImageData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Image;
import dLib.ui.themes.UITheme;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureThemeBinding;

public class ImageScreenEditorItem extends RenderableScreenEditorItem {
    /** Constructors */
    public ImageScreenEditorItem(TextureBinding textureBinding, int xPos, int yPos, int width, int height) {
        super(textureBinding, xPos, yPos, width, height);
    }

    public ImageScreenEditorItem(ImageData data){
        super(data);
    }

    /** Data */
    @Override
    public ImageData makeElementData() {
        return new ImageData();
    }

    @Override
    public void initializeElementData(UIElementData data) {
        super.initializeElementData(data);
        ImageData imageData = (ImageData) data;
    }

    @Override
    public ImageData getElementData() {
        return (ImageData) super.getElementData();
    }

    /** Copy */
    public static ScreenEditorItem makeNewInstance(ScreenEditorBaseScreen screenEditor){
        return new ImageScreenEditorItem(new TextureThemeBinding("whitePixel"), 0, 0, 75, 75);
    }

    @Override
    public Class<? extends UIElement> getLiveInstanceType() {
        return Image.class;
    }
}
