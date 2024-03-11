package dLib.tools.screeneditor.ui.items.editoritems;

import dLib.tools.screeneditor.ui.items.implementations.preview.ScreenEditorPreview;
import dLib.ui.elements.prefabs.Image;
import dLib.util.bindings.texture.TextureThemeBinding;

public class ImageScreenEditorItem extends ScreenEditorItem<Image, Image.ImageData> {
    //region Variables
    //endregion

    //region Constructors

    public ImageScreenEditorItem(){
        super(0, 0, 50, 50);
    }

    //endregion

    //region Methods

    @Override
    protected Image.ImageData makeDataType() {
        return new Image.ImageData();
    }

    //endregion
}
