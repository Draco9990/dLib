package dLib.tools.screeneditorold.ui.items.editoritems;

import dLib.ui.elements.prefabs.Image;
import dLib.util.ui.position.Pos;

public class ImageScreenEditorItem extends ScreenEditorItem<Image, Image.ImageData> {
    //region Variables
    //endregion

    //region Constructors

    public ImageScreenEditorItem(){
        super(Pos.px(0), Pos.px(0), 150, 150);
    }

    public ImageScreenEditorItem(Image.ImageData elementData) {
        super(elementData);
    }

    //endregion

    //region Methods

    @Override
    protected Image.ImageData makeDataType() {
        return new Image.ImageData();
    }

    //endregion
}
