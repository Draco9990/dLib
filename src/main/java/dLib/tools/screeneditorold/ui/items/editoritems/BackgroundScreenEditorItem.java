package dLib.tools.screeneditorold.ui.items.editoritems;

import dLib.tools.screeneditorold.ui.items.implementations.preview.ScreenEditorPreview;
import dLib.ui.elements.prefabs.Image;
import dLib.util.bindings.texture.TextureThemeBinding;
import dLib.util.ui.position.Pos;

public class BackgroundScreenEditorItem extends ScreenEditorItem<Image, Image.ImageData> {
    //region Variables
    //endregion

    //region Constructors

    public BackgroundScreenEditorItem(){
        super(Pos.px(0), Pos.px(0), ScreenEditorPreview.width, ScreenEditorPreview.height);
    }

    public BackgroundScreenEditorItem(Image.ImageData imageData){
        super(imageData);
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
