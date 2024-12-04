package dLib.tools.screeneditorold.ui.items.editoritems;

import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.ui.position.Pos;

public class InputfieldScreenEditorItem extends ScreenEditorItem<Inputfield, Inputfield.InputfieldData> {
    //region Variables
    //endregion

    //region Constructors

    public InputfieldScreenEditorItem(){
        super(Pos.px(0), Pos.px(0), 500, 75);
    }

    public InputfieldScreenEditorItem(Inputfield.InputfieldData elementData) {
        super(elementData);
    }

    //endregion

    //region Methods

    @Override
    protected Inputfield.InputfieldData makeDataType() {
        return new Inputfield.InputfieldData();
    }

    //endregion
}
