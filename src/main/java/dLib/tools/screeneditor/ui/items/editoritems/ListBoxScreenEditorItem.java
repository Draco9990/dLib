package dLib.tools.screeneditor.ui.items.editoritems;

import dLib.ui.elements.prefabs.HorizontalListBox;

public class ListBoxScreenEditorItem extends ScreenEditorItem<HorizontalListBox<Object>, HorizontalListBox.HorizontalListBoxData> {
    //region Variables
    //endregion

    //region Constructors

    public ListBoxScreenEditorItem(){
        super(0, 0, 500, 500);
    }

    //endregion

    //region Methods

    @Override
    protected HorizontalListBox.HorizontalListBoxData makeDataType() {
        return new HorizontalListBox.HorizontalListBoxData();
    }

    //endregion
}
