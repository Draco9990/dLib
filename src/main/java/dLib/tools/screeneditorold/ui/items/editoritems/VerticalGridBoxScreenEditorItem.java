package dLib.tools.screeneditorold.ui.items.editoritems;

import dLib.ui.elements.prefabs.VerticalGridBox;
import dLib.util.ui.position.Pos;

public class VerticalGridBoxScreenEditorItem extends ScreenEditorItem<VerticalGridBox<Object>, VerticalGridBox.VerticalGridBoxData> {
    //region Variables
    //endregion

    //region Constructors

    public VerticalGridBoxScreenEditorItem(){
        super(Pos.px(0), Pos.px(0), 500, 500);
    }

    public VerticalGridBoxScreenEditorItem(VerticalGridBox.VerticalGridBoxData elementData) {
        super(elementData);
    }

    //endregion

    //region Methods

    @Override
    protected VerticalGridBox.VerticalGridBoxData makeDataType() {
        return new VerticalGridBox.VerticalGridBoxData();
    }

    //endregion
}
