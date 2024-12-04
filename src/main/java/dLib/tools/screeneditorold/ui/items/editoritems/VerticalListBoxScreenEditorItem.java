package dLib.tools.screeneditorold.ui.items.editoritems;

import dLib.ui.elements.prefabs.VerticalListBox;
import dLib.util.ui.position.Pos;

public class VerticalListBoxScreenEditorItem extends ScreenEditorItem<VerticalListBox<Object>, VerticalListBox.VerticalListBoxData> {
    //region Variables
    //endregion

    //region Constructors

    public VerticalListBoxScreenEditorItem(){
        super(Pos.px(0), Pos.px(0), 500, 500);
    }

    public VerticalListBoxScreenEditorItem(VerticalListBox.VerticalListBoxData elementData) {
        super(elementData);
    }

    //endregion

    //region Methods

    @Override
    protected VerticalListBox.VerticalListBoxData makeDataType() {
        return new VerticalListBox.VerticalListBoxData();
    }

    //endregion
}
