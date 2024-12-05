package dLib.tools.screeneditorold.ui.items.editoritems;

import dLib.ui.elements.prefabs.HorizontalListBox;
import dLib.util.ui.position.Pos;

public class HorizontalListBoxScreenEditorItem extends ScreenEditorItem<HorizontalListBox<Object>, HorizontalListBox.HorizontalListBoxData> {
    //region Variables
    //endregion

    //region Constructors

    public HorizontalListBoxScreenEditorItem(){
        super(Pos.px(0), Pos.px(0), 500, 500);
    }

    public HorizontalListBoxScreenEditorItem(HorizontalListBox.HorizontalListBoxData data){
        super(data);
    }

    //endregion

    //region Methods

    @Override
    protected HorizontalListBox.HorizontalListBoxData makeDataType() {
        return new HorizontalListBox.HorizontalListBoxData();
    }

    //endregion
}
