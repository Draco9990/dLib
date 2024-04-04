package dLib.tools.screeneditor.ui.items.editoritems;

import dLib.ui.elements.prefabs.HorizontalListBox;

public class HorizontalListBoxScreenEditorItem extends ScreenEditorItem<HorizontalListBox<Object>, HorizontalListBox.HorizontalListBoxData> {
    //region Variables
    //endregion

    //region Constructors

    public HorizontalListBoxScreenEditorItem(){
        super(0, 0, 500, 500);
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
