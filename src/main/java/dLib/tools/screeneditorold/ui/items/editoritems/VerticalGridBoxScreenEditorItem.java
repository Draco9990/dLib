package dLib.tools.screeneditorold.ui.items.editoritems;

import dLib.ui.elements.prefabs.GridItemBox;
import dLib.util.ui.position.Pos;

public class VerticalGridBoxScreenEditorItem extends ScreenEditorItem<GridItemBox<Object>, GridItemBox.GridBoxData> {
    //region Variables
    //endregion

    //region Constructors

    public VerticalGridBoxScreenEditorItem(){
        super(Pos.px(0), Pos.px(0), 500, 500);
    }

    public VerticalGridBoxScreenEditorItem(GridItemBox.GridBoxData elementData) {
        super(elementData);
    }

    //endregion

    //region Methods

    @Override
    protected GridItemBox.GridBoxData makeDataType() {
        return new GridItemBox.GridBoxData();
    }

    //endregion
}
