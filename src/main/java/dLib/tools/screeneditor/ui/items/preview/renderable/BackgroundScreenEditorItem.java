package dLib.tools.screeneditor.ui.items.preview.renderable;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditor.screens.preview.ScreenEditorPreviewScreen;
import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.ImageData;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;
import dLib.util.bindings.texture.TextureThemeBinding;

public class BackgroundScreenEditorItem extends RenderableScreenEditorItem {
    /** Constructors */
    public BackgroundScreenEditorItem(){
        super(new TextureEmptyBinding(), ScreenEditorPreviewScreen.xOffset, ScreenEditorPreviewScreen.yOffset, ScreenEditorPreviewScreen.width, ScreenEditorPreviewScreen.height);

        setHoveredColor(Color.WHITE);
    }

    public BackgroundScreenEditorItem(TextureBinding textureBinding, int xPos, int yPos, int width, int height) {
        super(textureBinding, xPos, yPos, width, height);
    }

    public BackgroundScreenEditorItem(ImageData data){
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
    @Override
    public ScreenEditorItem makeCopy() {
        return (ScreenEditorItem) new BackgroundScreenEditorItem(sTexture.getCurrentValue(), x, y, width, height).setHoveredColor(getHoveredColor());
    }
}
