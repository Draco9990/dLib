package dLib.tools.screeneditor.ui.items.editoritems;

import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.ListBox;

public class ListBoxScreenEditorItem extends ScreenEditorItem<ListBox<Object>, ListBox.ListBoxData> {
    //region Variables
    //endregion

    //region Constructors

    public ListBoxScreenEditorItem(){
        super(0, 0, 500, 500);
    }

    //endregion

    //region Methods

    @Override
    protected ListBox.ListBoxData makeDataType() {
        return new ListBox.ListBoxData();
    }

    //endregion
}
