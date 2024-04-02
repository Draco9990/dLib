package dLib.tools.screeneditor.ui.items.editoritems;

import dLib.ui.elements.prefabs.VerticalListBox;

public class VerticalListBoxScreenEditorItem extends ScreenEditorItem<VerticalListBox<Object>, VerticalListBox.VerticalListBoxData> {
    //region Variables
    //endregion

    //region Constructors

    public VerticalListBoxScreenEditorItem(){
        super(0, 0, 500, 500);
    }

    //endregion

    //region Methods

    @Override
    protected VerticalListBox.VerticalListBoxData makeDataType() {
        return new VerticalListBox.VerticalListBoxData();
    }

    //endregion
}
