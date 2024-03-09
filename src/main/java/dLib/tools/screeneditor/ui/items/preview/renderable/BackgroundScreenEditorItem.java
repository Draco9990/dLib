package dLib.tools.screeneditor.ui.items.preview.renderable;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.implementations.preview.ScreenEditorPreview;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.BackgroundData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Image;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;

public class BackgroundScreenEditorItem extends ImageScreenEditorItem {
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
    public static ScreenEditorItem makeNewInstance(ScreenEditorBaseScreen screenEditor){
        return (ScreenEditorItem) new BackgroundScreenEditorItem(new TextureEmptyBinding(), ScreenEditorPreview.xOffset, ScreenEditorPreview.yOffset, ScreenEditorPreview.width, ScreenEditorPreview.height).setHoveredColor(Color.WHITE);
    }

    @Override
    public Class<? extends UIElement> getLiveInstanceType() {
        return Image.class;
    }
}
