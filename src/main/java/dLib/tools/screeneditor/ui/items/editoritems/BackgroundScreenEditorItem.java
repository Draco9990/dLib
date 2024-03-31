package dLib.tools.screeneditor.ui.items.editoritems;

import dLib.tools.screeneditor.ui.items.implementations.preview.ScreenEditorPreview;
import dLib.ui.elements.prefabs.Image;
import dLib.ui.elements.prefabs.TextButton;
import dLib.util.bindings.texture.TextureThemeBinding;

public class BackgroundScreenEditorItem extends ScreenEditorItem<Image, Image.ImageData> {
    //region Variables
    //endregion

    //region Constructors

    public BackgroundScreenEditorItem(){
        super(0, 0, ScreenEditorPreview.width, ScreenEditorPreview.height);
    }

    //endregion

    //region Methods

    @Override
    protected Image.ImageData makeDataType() {
        Image.ImageData data = new Image.ImageData();
        data.textureBinding.setValue(new TextureThemeBinding("background"));
        return data;
    }

    //endregion
}
