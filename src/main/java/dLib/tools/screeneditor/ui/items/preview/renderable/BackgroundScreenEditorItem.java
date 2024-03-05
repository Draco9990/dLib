package dLib.tools.screeneditor.ui.items.preview.renderable;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditor.screens.preview.ScreenEditorPreviewScreen;
import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.BackgroundData;
import dLib.ui.data.prefabs.ImageData;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;
import dLib.util.bindings.texture.TextureThemeBinding;

public class BackgroundScreenEditorItem extends RenderableScreenEditorItem {
    /** Constructors */
    public BackgroundScreenEditorItem(TextureBinding textureBinding, int xPos, int yPos, int width, int height) {
        super(textureBinding, xPos, yPos, width, height);
    }

    public BackgroundScreenEditorItem(BackgroundData data){
        super(data);
    }

    /** Data */
    @Override
    public BackgroundData makeElementData() {
        return new BackgroundData();
    }

    @Override
    public void initializeElementData(UIElementData data) {
        super.initializeElementData(data);
        BackgroundData imageData = (BackgroundData) data;
    }

    @Override
    public BackgroundData getElementData() {
        return (BackgroundData) super.getElementData();
    }

    /** Copy */
    public static ScreenEditorItem makeNewInstance(){
        return (ScreenEditorItem) new BackgroundScreenEditorItem(new TextureEmptyBinding(), ScreenEditorPreviewScreen.xOffset, ScreenEditorPreviewScreen.yOffset, ScreenEditorPreviewScreen.width, ScreenEditorPreviewScreen.height).setHoveredColor(Color.WHITE);
    }
}
