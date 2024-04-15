package dLib.tools.screeneditor.ui.items.editoritems;

import dLib.ui.elements.prefabs.VerticalGridBox;
import dLib.ui.elements.prefabs.VerticalListBox;

public class VerticalGridBoxScreenEditorItem extends ScreenEditorItem<VerticalGridBox<Object>, VerticalGridBox.VerticalGridBoxData> {
    //region Variables
    //endregion

    //region Constructors

    public VerticalGridBoxScreenEditorItem(){
        super(0, 0, 500, 500);
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
